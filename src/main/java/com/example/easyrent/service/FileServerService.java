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
import java.util.function.Consumer;

@Service
public class FileServerService {
    public static final String imageUploadDir = "image_uploads";
    public static final String videoUploadDir = "video_uploads";

    public String saveAvatarUser(MultipartFile file, Integer userId) {
        Path userDirPath = Paths.get(imageUploadDir, userId.toString());
        return saveFileToDir(file, userDirPath, this::validateImage);
    }

    // Hàm lưu ảnh phòng
    public String saveImagesRoom(MultipartFile file, Integer userId, Integer roomId) {
        Path roomDirPath = Paths.get(imageUploadDir, userId.toString(), "room" + roomId);
        return saveFileToDir(file, roomDirPath, this::validateImage);
    }

    // Hàm lưu video phòng
    public String saveVideosRoom(MultipartFile file, Integer userId, Integer roomId) {
        Path roomDirPath = Paths.get(videoUploadDir, userId.toString(), "room" + roomId);
        return saveFileToDir(file, roomDirPath, this::validateVideo);
    }
    
    private String saveFileToDir(MultipartFile file, Path dirPath, Consumer<MultipartFile> validator ) {
        validator.accept(file);  // Validate file trước khi lưu

        try {
            Files.createDirectories(dirPath);

            // Tạo tên file duy nhất
            String fileName = System.currentTimeMillis() + "_" + UUID.randomUUID().toString() + "." + getFileExtension(Objects.requireNonNull(file.getOriginalFilename()));

            Path filePath = dirPath.resolve(fileName);

            // Lưu file vào thư mục
            Files.copy(file.getInputStream(), filePath);

            // Trả về đường dẫn URL để lưu vào DB
            String relativePath = filePath.toString().replace(System.getProperty("user.dir") + "/", "").replace("\\", "/");

            return "/" + relativePath;
        } catch (IOException e) {
            throw new RuntimeException("Không thể lưu file. Lỗi: " + e.getMessage());
        }
    }


    // Xác thực file
    private void validateImage(MultipartFile file) {
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

    private void validateVideo(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        System.out.println("Validating video: " + fileName);

        // Kiểm tra tên file có hợp lệ không
        if (fileName == null || fileName.isEmpty()) {
            throw new RuntimeException("Tên file không hợp lệ");
        }

        // Kiểm tra phần mở rộng có hợp lệ không
        String fileExtension = getFileExtension(fileName);
        if (!isAllowedVideoExtension(fileExtension)) {
            throw new RuntimeException("Phần mở rộng file không hợp lệ");
        }

        // Kiểm tra kích thước file
        double fileSizeMB = (double) (file.getSize() / 1_048_576);
        if (fileSizeMB > 100) {  // Giới hạn 50MB cho video
            throw new RuntimeException("Kích thước video vượt quá giới hạn cho phép (50MB)");
        }
    }

    // Kiểm tra phần mở rộng hợp lệ
    private boolean isAllowedExtension(String fileExtension) {
        List<String> allowedExtensions = List.of("png", "jpg", "jpeg");
        return allowedExtensions.contains(fileExtension.toLowerCase());
    }

    private boolean isAllowedVideoExtension(String fileExtension) {
        List<String> allowedExtensions = List.of("mp4", "avi", "mov", "wmv", "flv", "mkv");
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
