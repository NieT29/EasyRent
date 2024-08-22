package com.example.easyrent.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileServerService {
    public static final String uploadDir = "image_uploads";

    public String saveFile(MultipartFile file, Integer userId) {
        validateFile(file);  // Validate file trước khi lưu

        try {
            // Tạo thư mục cho user nếu chưa có
            Path userPath = Paths.get(uploadDir, userId.toString());
            System.out.println(userPath);
            Files.createDirectories(userPath);

            // Tạo tên file duy nhất

            String fileName = System.currentTimeMillis() + "_" + UUID.randomUUID().toString() + "." + getFileExtension(Objects.requireNonNull(file.getOriginalFilename()));

            Path filePath = userPath.resolve(fileName);

            // Lưu file vào thư mục
            Files.copy(file.getInputStream(), filePath);

            // Trả về đường dẫn URL để lưu vào DB
            return "/" + uploadDir + "/" + userId + "/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Could not store file. Error: " + e.getMessage());
        }
    }

    // Xác thực file
    private void validateFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();

        // Kiểm tra tên file có hợp lệ không
        if (fileName == null || fileName.isEmpty()) {
            throw new RuntimeException("Tên file không hợp lệ");
        }

        // Kiểm tra phần mở rộng có hợp lệ không
        String fileExtension = getFileExtension(fileName);
        if (!isAllowedExtension(fileExtension)) {
            throw new RuntimeException("Phần mở rộng file không hợp lệ");
        }

        // Kiểm tra kích thước file
        double fileSizeMB = (double) (file.getSize() / 1_048_576);
        if (fileSizeMB > 2) {  // Giới hạn 2MB
            throw new RuntimeException("Kích thước file vượt quá giới hạn cho phép (2MB)");
        }
    }

    // Kiểm tra phần mở rộng hợp lệ
    private boolean isAllowedExtension(String fileExtension) {
        List<String> allowedExtensions = List.of("png", "jpg", "jpeg");
        return allowedExtensions.contains(fileExtension.toLowerCase());
    }

    // Lấy phần mở rộng file
    private String getFileExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf(".");
        return (lastIndex != -1) ? fileName.substring(lastIndex + 1) : "";
    }

    // Xóa file
    public void deleteFile(String fileUrl) {
        try {
            // Xác định đường dẫn thực tế của file dựa trên URL
            Path filePath = Paths.get(System.getProperty("user.dir"), fileUrl);

            // Kiểm tra nếu file tồn tại và xóa
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            } else {
                throw new RuntimeException("File không tồn tại hoặc đã bị xóa");
            }
        } catch (IOException e) {
            throw new RuntimeException("Không thể xóa file. Lỗi: " + e.getMessage());
        }
    }
}
