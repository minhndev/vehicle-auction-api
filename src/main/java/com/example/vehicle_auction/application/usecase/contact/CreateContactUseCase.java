package com.example.vehicle_auction.application.usecase.contact;

import com.example.vehicle_auction.application.dto.contact.ContactRequest;
import com.example.vehicle_auction.application.dto.contact.ContactResponse;
import com.example.vehicle_auction.application.mapper.ContactMapper;
import com.example.vehicle_auction.application.port.out.EmailSenderPort;
import com.example.vehicle_auction.domain.enums.ContactStatus;
import com.example.vehicle_auction.domain.model.ContactModel;
import com.example.vehicle_auction.domain.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateContactUseCase {
    private final ContactRepository contactRepository;
    private final ContactMapper contactMapper;
    private final EmailSenderPort emailSenderPort;

    @Transactional
    public ContactResponse execute(ContactRequest request) {
        ContactModel model = contactMapper.toDomain(request);

        if (model.getStatus() == null) {
            model.setStatus(ContactStatus.PENDING);
        }

        ContactModel savedModel = contactRepository.save(model);

        sendConfirmationEmail(savedModel);

        return contactMapper.toResponse(savedModel);
    }

    private void sendConfirmationEmail(ContactModel contact) {
        String subject = "[Vehicle Auction] Xác nhận yêu cầu: " + contact.getSubject();

        String body = """
                Xin chào %s,
                
                Cảm ơn bạn đã liên hệ với chúng tôi. Hệ thống đã ghi nhận yêu cầu của bạn với nội dung chi tiết như sau:
                
                -------------------------------------------------
                Chủ đề hỗ trợ: %s
                Nội dung yêu cầu:
                %s
                -------------------------------------------------
                
                Thông tin liên hệ của bạn:
                - Số điện thoại: %s
                - Email: %s
                
                Chúng tôi sẽ kiểm tra và phản hồi lại bạn qua email hoặc số điện thoại trên trong vòng 24 giờ làm việc.
                
                Trân trọng,
                Đội ngũ chăm sóc khách hàng Vehicle Auction
                """.formatted(
                contact.getFullName(),
                contact.getSubject(),
                contact.getContent(),
                contact.getPhoneNumber(),
                contact.getEmail()
        );

        emailSenderPort.sendEmail(contact.getEmail(), subject, body);
    }
}
