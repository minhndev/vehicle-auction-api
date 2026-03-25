package com.example.vehicle_auction.application.usecase.bid;

import java.util.UUID;

final class BidRealtimeRedisKeys {
    private BidRealtimeRedisKeys() {
    }

    public static String bidRankingKey(UUID auctionId) {
        return "auction:" + auctionId + ":bids:zset";
    }

    public static String bidMetaKey(UUID auctionId, UUID bidId) {
        return "auction:" + auctionId + ":bids:meta:" + bidId;
    }

    public static String currentPriceKey(UUID auctionId) {
        return "auction:" + auctionId + ":currentPrice";
    }

    public static String versionKey(UUID auctionId) {
        return "auction:" + auctionId + ":version";
    }

    public static String bidEventsChannel(UUID auctionId) {
        return "auction:" + auctionId + ":events:bids";
    }
}



