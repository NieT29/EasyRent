package com.example.easyrent.service.Impl;

import com.example.easyrent.entity.ImageRoom;
import com.example.easyrent.entity.Room;
import com.example.easyrent.entity.User;
import com.example.easyrent.repository.ImageRoomRepository;
import com.example.easyrent.security.SecurityUtils;
import com.example.easyrent.service.FileServerService;
import com.example.easyrent.service.ImageRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ImageRoomServiceImpl implements ImageRoomService {
    private final ImageRoomRepository imageRoomRepository;
    private final FileServerService fileServerService;

    @Override
    public List<String> uploadRoomImage(List<MultipartFile> newImages, Integer roomId) {
        User currentUser = SecurityUtils.getCurrentUser();
        List<String> imageUrls = new ArrayList<>();

        String roomFolderName = "room" + roomId;
        Path roomDirPath = Paths.get(FileServerService.imageUploadDir, currentUser.getId().toString(), roomFolderName);
        try {
            Files.createDirectories(roomDirPath);
        } catch (IOException e) {
            throw new RuntimeException("Could not create directory for room. Error: " + e.getMessage());
        }

        for (MultipartFile image : newImages) {
            // Tính toán hash của file ảnh
            String fileHash = calculateFileHash(image);

            // Kiểm tra xem file có cùng hash đã tồn tại trong thư mục `room{roomId}` chưa
            String imageUrl = findExistingFileUrlByHash(roomDirPath, fileHash);

            if (imageUrl == null) {
                // Nếu ảnh chưa tồn tại, lưu file mới
                imageUrl = fileServerService.saveImagesRoom(image, currentUser.getId(), roomId);
            }

            imageUrls.add(imageUrl);

            // Tạo đối tượng ImageRoom và liên kết với Room bằng roomId
            ImageRoom imageRoom = ImageRoom.builder()
                    .img_path(imageUrl)
                    .room(Room.builder().id(roomId).build())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            imageRoomRepository.save(imageRoom);
        }
        return imageUrls;
    }



    private String calculateFileHash(MultipartFile file) {
        try {
            byte[] fileBytes = file.getBytes();
            return calculateFileHash(fileBytes);
        } catch (IOException e) {
            throw new RuntimeException("Could not calculate file hash from MultipartFile. Error: " + e.getMessage());
        }
    }

    private String calculateFileHash(byte[] fileBytes) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(fileBytes);
            return HexFormat.of().formatHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Could not calculate file hash. Error: " + e.getMessage());
        }
    }



    private String findExistingFileUrlByHash(Path roomDirPath, String fileHash) {
        try (Stream<Path> files = Files.list(roomDirPath)) {
            return files
                    .filter(p -> p.toFile().isFile())
                    .filter(p -> {
                        try {
                            // Đọc nội dung file từ Path
                            byte[] fileBytes = Files.readAllBytes(p);
                            // Tính toán hash của file và so sánh với hash cần tìm
                            return calculateFileHash(fileBytes).equals(fileHash);
                        } catch (IOException e) {
                            return false;
                        }
                    })
                    .map(p -> "/" + FileServerService.imageUploadDir + "/" + roomDirPath.getParent().getFileName().toString() + "/" + roomDirPath.getFileName().toString() + "/" + p.getFileName().toString())
                    .findFirst()
                    .orElse(null);
        } catch (IOException e) {
            throw new RuntimeException("Error while searching for existing file by hash. Error: " + e.getMessage());
        }
    }
}
