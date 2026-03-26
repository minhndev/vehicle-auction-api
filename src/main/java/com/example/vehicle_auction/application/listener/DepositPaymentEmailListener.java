package com.example.vehicle_auction.application.listener;

import com.example.vehicle_auction.domain.enums.DepositStatus;
import com.example.vehicle_auction.domain.event.DepositPaymentProcessedEvent;
import com.example.vehicle_auction.domain.exception.AppException;
import com.example.vehicle_auction.domain.exception.ErrorCode;
import com.example.vehicle_auction.domain.model.AccountModel;
import com.example.vehicle_auction.domain.repository.AccountRepository;
import com.example.vehicle_auction.infrastructure.configuration.RabbitMQConfig;
import com.example.vehicle_auction.infrastructure.messaging.RabbitMQProducer;
import com.example.vehicle_auction.infrastructure.messaging.dto.MailMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Slf4j
@Component
@RequiredArgsConstructor
public class DepositPaymentEmailListener {
    private final AccountRepository accountRepository;
    private final RabbitMQProducer rabbitMQProducer;

    @EventListener
    public void handleDepositPaymentEvent(DepositPaymentProcessedEvent event) {
        try {
            AccountModel account = accountRepository.findById(event.accountId())
                    .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));

            String formattedDate = event.occurredAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

            String subject = "";
            String body = "";

            if (event.depositStatus() == DepositStatus.PAID) {
                subject = "[Vehicle Auction] Xác nhận đặt cọc thành công!";
                body = """
                        Xin chào %s,
                        
                        Chúc mừng bạn đã hoàn tất đặt cọc để tham gia phiên đấu giá!
                        
                        Thông tin giao dịch:
                        -------------------------------------------------
                        - Mã phiên đấu giá: %s
                        - Mã tham chiếu giao dịch: %s
                        - Thời gian giao dịch: %s
                        - Trạng thái: THÀNH CÔNG
                        -------------------------------------------------
                        
                        Bạn hiện đã đủ điều kiện để tham gia trả giá cho phiên đấu giá này khi thời gian bắt đầu. Vui lòng theo dõi ứng dụng để không bỏ lỡ cơ hội sở hữu chiếc xe ưng ý.
                        
                        Chúc bạn đấu giá thành công!
                        
                        Trân trọng,
                        Đội ngũ Vehicle Auction
                        """.formatted(
                        account.getEmail(),
                        event.auctionId().toString(),
                        event.transactionRef(),
                        formattedDate
                );

            } else if (event.depositStatus() == DepositStatus.FAILED) {
                subject = "[Vehicle Auction] Thông báo giao dịch đặt cọc thất bại";
                body = """
                        Xin chào %s,
                        
                        Hệ thống ghi nhận giao dịch đặt cọc cho phiên đấu giá của bạn đã KHÔNG THÀNH CÔNG.
                        
                        Thông tin giao dịch:
                        -------------------------------------------------
                        - Mã phiên đấu giá: %s
                        - Mã tham chiếu giao dịch: %s
                        - Thời gian giao dịch: %s
                        - Trạng thái: THẤT BẠI
                        - Mã lỗi VNPAY: %s
                        -------------------------------------------------
                        
                        Nguyên nhân thất bại có thể do số dư không đủ, giao dịch bị hủy, hoặc lỗi kết nối từ ngân hàng. 
                        Vui lòng kiểm tra lại tài khoản ngân hàng của bạn và thực hiện đặt cọc lại trên hệ thống để không bỏ lỡ phiên đấu giá.
                        
                        Nếu cần hỗ trợ, vui lòng gửi yêu cầu qua form Liên Hệ trên website.
                        
                        Trân trọng,
                        Đội ngũ Vehicle Auction
                        """.formatted(
                        account.getEmail(),
                        event.auctionId().toString(),
                        event.transactionRef(),
                        formattedDate,
                        event.vnpResponseCode()
                );
            } else {
                return;
            }

            MailMessage mailMessage = new MailMessage(account.getEmail(), subject, body);
            rabbitMQProducer.sendMessage(RabbitMQConfig.RK_MAIL_DEPOSIT, mailMessage);
            log.info("Successfully pushed {} email message to RabbitMQ for user {} (TxnRef: {})",
                    event.depositStatus(), account.getEmail(), event.transactionRef());

        } catch (Exception e) {
            log.error("Failed to send {} email for depositId: {}. Reason: {}",
                    event.depositStatus(), event.depositId(), e.getMessage(), e);
        }
    }
}