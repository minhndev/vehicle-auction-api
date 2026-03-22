package com.example.vehicle_auction.application.port.out;

import java.io.InputStream;

public interface FileStoragePort {
    /**
     * @param fileName Original file name (e.g., car.jpg)
     * @param contentType File type (e.g., image/jpeg)
     * @param inputStream Data stream of the file
     * @param contentLength File size (for S3 optimized upload)
     * @return Public URL of the image after successful upload
     */
    String uploadFile(String fileName, String contentType, InputStream inputStream, long contentLength);

    void deleteFile(String fileUrl);
}
