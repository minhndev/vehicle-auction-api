package com.example.vehicle_auction.application.usecase.file;

import com.example.vehicle_auction.application.dto.file.UploadFileResponse;
import com.example.vehicle_auction.application.port.out.FileStoragePort;
import com.example.vehicle_auction.domain.exception.AppException;
import com.example.vehicle_auction.domain.exception.ErrorCode;
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

        if (size > 5 * 1024 * 1024) {
            throw new AppException(ErrorCode.FILE_TOO_LARGE);
        }

        if (contentType == null || !contentType.startsWith("image/")) {
            throw new RuntimeException("Only image formats are allowed (JPG, PNG,...)");
        }

        String fileUrl = fileStoragePort.uploadFile(fileName, contentType, inputStream, size);

        return new UploadFileResponse(fileUrl);
    }
}
