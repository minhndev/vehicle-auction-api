package com.example.vehicle_auction.application.usecase.file;

import com.example.vehicle_auction.application.dto.file.UploadFileResponse;
import com.example.vehicle_auction.application.port.out.FileStoragePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class UploadImageUseCase {
    private final FileStoragePort fileStoragePort;

    public UploadFileResponse execute(String fileName, String contentType, InputStream inputStream, long size) {
        log.info("Processing upload for file: {}, size: {} bytes", fileName, size);

        // 1. Validate dung lượng (Ví dụ: Giới hạn 5MB)
        if (size > 5 * 1024 * 1024) {
            // Khuyến nghị: Bạn thêm ErrorCode.FILE_TOO_LARGE vào enum nhé
            throw new RuntimeException("Kích thước file vượt quá 5MB");
        }

        // 2. Validate định dạng (Chỉ cho phép ảnh)
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new RuntimeException("Chỉ cho phép upload định dạng hình ảnh (JPG, PNG,...)");
        }

        // 3. Gọi Port để upload lên S3
        String fileUrl = fileStoragePort.uploadFile(fileName, contentType, inputStream, size);

        return new UploadFileResponse(fileUrl);
    }
}
