package com.example.vehicle_auction.application.usecase.bid;

import com.example.vehicle_auction.application.dto.bid.AuctionBidRealtimeMessage;
import com.example.vehicle_auction.application.dto.bid.BidHistoryItemResponse;
import com.example.vehicle_auction.domain.enums.AuctionStatus;
import com.example.vehicle_auction.domain.model.AuctionModel;
import com.example.vehicle_auction.domain.model.BidModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.RedisCommandTimeoutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BidRealtimeStore {

    private static final Long LUA_OK = 1L;
    private static final Long LUA_BID_TOO_LOW = -1L;
    private static final Long LUA_VERSION_MISMATCH = -2L;
    private static final Long LUA_AUCTION_INACTIVE = -3L;

    private static final String UPSERT_BID_LUA = """
            local amount = tonumber(ARGV[1])
            local minNextBid = tonumber(ARGV[2])
            local expectedVersion = tonumber(ARGV[3])
            local isActive = tonumber(ARGV[4])

            if isActive ~= 1 then
                return -3
            end

            if amount < minNextBid then
                return -1
            end

            local currentVersion = redis.call('GET', KEYS[3])
            if currentVersion and tonumber(currentVersion) ~= expectedVersion then
                return -2
            end

            redis.call('ZADD', KEYS[1], amount, ARGV[5])
            redis.call('HSET', KEYS[4],
                'bidId', ARGV[5],
                'auctionId', ARGV[6],
                'bidderMask', ARGV[7],
                'amount', ARGV[1],
                'bidStatus', ARGV[8],
                'createdAt', ARGV[9]
            )
            redis.call('SET', KEYS[2], ARGV[1])

            if currentVersion then
                redis.call('INCR', KEYS[3])
            else
                redis.call('SET', KEYS[3], expectedVersion + 1)
            end

            redis.call('PUBLISH', KEYS[5], ARGV[10])
            return 1
            """;

    private static final DefaultRedisScript<Long> UPSERT_BID_SCRIPT = new DefaultRedisScript<>(UPSERT_BID_LUA, Long.class);

    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;

    public enum RedisBidWriteStatus {
        OK,
        BID_TOO_LOW,
        VERSION_MISMATCH,
        AUCTION_INACTIVE,
        REDIS_ERROR,
        UNKNOWN
    }

    public Optional<List<BidHistoryItemResponse>> getTopBids(UUID auctionId, int topN) {
        String rankingKey = BidRealtimeRedisKeys.bidRankingKey(auctionId);
        try {
            Boolean exists = stringRedisTemplate.hasKey(rankingKey);
            if (!exists) {
                log.info("Redis bid cache miss: ranking key not found. key={}", rankingKey);
                return Optional.empty();
            }

            Set<String> bidIds = stringRedisTemplate.opsForZSet().reverseRange(rankingKey, 0, Math.max(0, topN - 1));
            if (bidIds == null || bidIds.isEmpty()) {
                return Optional.of(List.of());
            }

            List<BidHistoryItemResponse> result = new ArrayList<>();
            int rank = 1;
            for (String bidIdValue : bidIds) {
                UUID bidId;
                try {
                    bidId = UUID.fromString(bidIdValue);
                } catch (IllegalArgumentException ex) {
                    log.error("Redis deserialize fail: invalid bidId in zset. auctionId={}, member={}", auctionId, bidIdValue, ex);
                    continue;
                }

                String bidMetaKey = BidRealtimeRedisKeys.bidMetaKey(auctionId, bidId);
                Map<Object, Object> raw = stringRedisTemplate.opsForHash().entries(bidMetaKey);
                if (raw.isEmpty()) {
                    log.warn("Redis key missing for bid metadata. auctionId={}, bidMetaKey={}", auctionId, bidMetaKey);
                    continue;
                }

                BidHistoryItemResponse item = toHistoryItem(raw, rank);
                if (item != null) {
                    result.add(item);
                    rank++;
                }
            }
            return Optional.of(result);
        } catch (RedisCommandTimeoutException ex) {
            log.error("Redis timeout while reading top bids. auctionId={}", auctionId, ex);
            return Optional.empty();
        } catch (RedisConnectionFailureException ex) {
            log.error("Redis connection failure while reading top bids. auctionId={}", auctionId, ex);
            return Optional.empty();
        } catch (DataAccessException ex) {
            log.error("Redis access error while reading top bids. auctionId={}", auctionId, ex);
            return Optional.empty();
        }
    }

    public void warmupFromDb(UUID auctionId, List<BidHistoryItemResponse> items, BigDecimal currentPrice, Integer version) {
        String rankingKey = BidRealtimeRedisKeys.bidRankingKey(auctionId);
        try {
            stringRedisTemplate.delete(rankingKey);
            for (BidHistoryItemResponse item : items) {
                String bidId = item.bidId().toString();
                stringRedisTemplate.opsForZSet().add(rankingKey, bidId, item.amount().doubleValue());

                String metaKey = BidRealtimeRedisKeys.bidMetaKey(auctionId, item.bidId());
                Map<String, String> values = new LinkedHashMap<>();
                values.put("bidId", bidId);
                values.put("auctionId", auctionId.toString());
                values.put("bidderMask", item.bidderMask());
                values.put("amount", item.amount().toPlainString());
                values.put("bidStatus", item.bidStatus());
                values.put("createdAt", item.createdAt().toString());
                stringRedisTemplate.opsForHash().putAll(metaKey, values);
            }

            if (currentPrice != null) {
                stringRedisTemplate.opsForValue().set(BidRealtimeRedisKeys.currentPriceKey(auctionId), currentPrice.toPlainString());
            }
            if (version != null) {
                stringRedisTemplate.opsForValue().set(BidRealtimeRedisKeys.versionKey(auctionId), String.valueOf(version));
            }
        } catch (Exception ex) {
            log.warn("Redis warmup failed for auctionId={}. Continue with DB data.", auctionId, ex);
        }
    }

    public RedisBidWriteStatus writeBidAtomically(
            AuctionModel auction,
            BidModel bid,
            BigDecimal minNextBid,
            Integer expectedVersion
    ) {
        UUID auctionId = auction.getId();
        UUID bidId = bid.getId();

        String payload;
        try {
            payload = objectMapper.writeValueAsString(new AuctionBidRealtimeMessage(
                    auctionId,
                    auction.getCurrentPrice(),
                    "Có giá mới: " + auction.getCurrentPrice(),
                    bidId,
                    toBidderMask(bid.getBidderId()),
                    bid.getAmount(),
                    bid.getCreatedAt()
            ));
        } catch (JsonProcessingException ex) {
            log.error("Failed to serialize bid realtime payload. auctionId={}, bidId={}", auctionId, bidId, ex);
            return RedisBidWriteStatus.UNKNOWN;
        }

        List<String> keys = List.of(
                BidRealtimeRedisKeys.bidRankingKey(auctionId),
                BidRealtimeRedisKeys.currentPriceKey(auctionId),
                BidRealtimeRedisKeys.versionKey(auctionId),
                BidRealtimeRedisKeys.bidMetaKey(auctionId, bidId),
                BidRealtimeRedisKeys.bidEventsChannel(auctionId)
        );

        try {
            Long result = stringRedisTemplate.execute(
                    UPSERT_BID_SCRIPT,
                    keys,
                    bid.getAmount().toPlainString(),
                    minNextBid.toPlainString(),
                    String.valueOf(expectedVersion == null ? 0 : expectedVersion),
                    auction.getStatus() == AuctionStatus.ACTIVE ? "1" : "0",
                    bidId.toString(),
                    auctionId.toString(),
                    toBidderMask(bid.getBidderId()),
                    bid.getStatus().name(),
                    bid.getCreatedAt().toString(),
                    payload
            );

            if (LUA_OK.equals(result)) {
                return RedisBidWriteStatus.OK;
            }
            if (LUA_BID_TOO_LOW.equals(result)) {
                log.warn("Redis script rejected bid: amount lower than min next bid. auctionId={}, bidId={}", auctionId, bidId);
                return RedisBidWriteStatus.BID_TOO_LOW;
            }
            if (LUA_VERSION_MISMATCH.equals(result)) {
                log.warn("Redis script rejected bid due to stale version. auctionId={}, bidId={}, expectedVersion={}", auctionId, bidId, expectedVersion);
                return RedisBidWriteStatus.VERSION_MISMATCH;
            }
            if (LUA_AUCTION_INACTIVE.equals(result)) {
                log.warn("Redis script rejected bid because auction is inactive. auctionId={}, bidId={}", auctionId, bidId);
                return RedisBidWriteStatus.AUCTION_INACTIVE;
            }

            log.error("Redis script returned unexpected result for bid write. auctionId={}, bidId={}, result={}", auctionId, bidId, result);
            return RedisBidWriteStatus.UNKNOWN;
        } catch (RedisCommandTimeoutException ex) {
            log.error("Redis timeout while writing bid script. auctionId={}, bidId={}", auctionId, bidId, ex);
            return RedisBidWriteStatus.REDIS_ERROR;
        } catch (RedisConnectionFailureException ex) {
            log.error("Redis connection failure while writing bid script. auctionId={}, bidId={}", auctionId, bidId, ex);
            return RedisBidWriteStatus.REDIS_ERROR;
        } catch (DataAccessException ex) {
            log.error("Redis script error while writing bid. auctionId={}, bidId={}", auctionId, bidId, ex);
            return RedisBidWriteStatus.REDIS_ERROR;
        }
    }

    private BidHistoryItemResponse toHistoryItem(Map<Object, Object> raw, int rank) {
        try {
            UUID bidId = UUID.fromString(String.valueOf(raw.get("bidId")));
            UUID auctionId = UUID.fromString(String.valueOf(raw.get("auctionId")));
            String bidderMask = String.valueOf(raw.get("bidderMask"));
            BigDecimal amount = new BigDecimal(String.valueOf(raw.get("amount")));
            String bidStatus = String.valueOf(raw.get("bidStatus"));
            LocalDateTime createdAt = LocalDateTime.parse(String.valueOf(raw.get("createdAt")));

            return new BidHistoryItemResponse(
                    bidId,
                    auctionId,
                    bidderMask,
                    amount,
                    bidStatus,
                    rank,
                    rank == 1,
                    createdAt
            );
        } catch (Exception ex) {
            log.error("Redis deserialize fail for bid history item. raw={}", raw, ex);
            return null;
        }
    }

    private String toBidderMask(UUID bidderId) {
        String raw = bidderId.toString().replace("-", "");
        String suffix = raw.substring(Math.max(0, raw.length() - 6)).toUpperCase();
        return "BIDDER-" + suffix;
    }
}







