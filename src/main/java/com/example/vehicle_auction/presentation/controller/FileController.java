package com.example.vehicle_auction.presentation.controller;

import com.example.vehicle_auction.application.dto.file.UploadFileResponse;
import com.example.vehicle_auction.application.usecase.file.UploadImageUseCase;
import com.example.vehicle_auction.domain.exception.AppException;
import com.example.vehicle_auction.domain.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    private final UploadImageUseCase uploadImageUseCase;

    @PostMapping("/upload")
    public ResponseEntity<UploadFileResponse> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            // Tách các tham số cơ bản truyền cho Use Case để đảm bảo Clean Architecture
            UploadFileResponse response = uploadImageUseCase.execute(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getInputStream(),
                    file.getSize()
            );
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
        }
    }
}
