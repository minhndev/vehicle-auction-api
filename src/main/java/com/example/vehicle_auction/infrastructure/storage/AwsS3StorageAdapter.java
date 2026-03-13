package com.example.vehicle_auction.infrastructure.storage;

import com.example.vehicle_auction.application.port.out.FileStoragePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.InputStream;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class AwsS3StorageAdapter implements FileStoragePort {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.s3.endpoint}")
    private String endpoint;

    @Override
    public String uploadFile(String fileName, String contentType, InputStream inputStream, long contentLength) {
        try {
            // Rename file to avoid duplication
            String uniqueFileName = "products/" + UUID.randomUUID() + "-" + fileName.replaceAll("\\s+", "_");

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(uniqueFileName)
                    .contentType(contentType)
                    // .acl(ObjectCannedACL.PUBLIC_READ)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, contentLength));


//            String fileUrl = String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, uniqueFileName);
//            log.info("Uploaded file to S3 successfully: {}", fileUrl);

            String fileUrl = String.format("%s/%s/%s", endpoint, bucketName, uniqueFileName);
            log.info("Uploaded file successfully to MinIO: {}", fileUrl);
            return fileUrl;

        } catch (Exception e) {
            log.error("Failed to upload file", e);
            throw new RuntimeException("Error uploading image to storage system.", e);
        }
    }

    @Override
    public void deleteFile(String fileUrl) {
        try {
//            String key = fileUrl.substring(fileUrl.indexOf("amazonaws.com/") + 14);

            String prefix = String.format("%s/%s/", endpoint, bucketName);
            String key = fileUrl.replace(prefix, "");

            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
            log.info("Deleted file: {}", key);
        } catch (Exception e) {
            log.error("Failed to delete file: {}", fileUrl, e);
        }
    }
}
