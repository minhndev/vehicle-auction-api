package com.example.vehicle_auction.application.port.out;

import java.io.InputStream;

public interface FileStoragePort {
    /**
     * @param fileName Tên file gốc (ví dụ: car.jpg)
     * @param contentType Kiểu file (ví dụ: image/jpeg)
     * @param inputStream Luồng dữ liệu của file
     * @param contentLength Kích thước file (để S3 tối ưu upload)
     * @return URL public của ảnh sau khi upload thành công
     */
    String uploadFile(String fileName, String contentType, InputStream inputStream, long contentLength);

    void deleteFile(String fileUrl);
}
