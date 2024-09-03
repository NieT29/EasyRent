package com.example.easyrent.service.Impl;

import com.example.easyrent.entity.Room;
import com.example.easyrent.entity.User;
import com.example.easyrent.entity.VideoRoom;
import com.example.easyrent.repository.VideoRoomRepository;
import com.example.easyrent.security.SecurityUtils;
import com.example.easyrent.service.FileServerService;
import com.example.easyrent.service.VideoRoomService;
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
public class VideoRoomServiceImpl implements VideoRoomService {
    private final VideoRoomRepository videoRoomRepository;
    private final FileServerService fileServerService;

    @Override
    public List<String> uploadRoomVideo(List<MultipartFile> newVideos, Integer roomId) {
        User currentUser = SecurityUtils.getCurrentUser();
        List<String> videoUrls = new ArrayList<>();

        String roomFolderName = "room" + roomId;

        Path roomDirPath = Paths.get(FileServerService.videoUploadDir, currentUser.getId().toString(), roomFolderName);
        try {
            Files.createDirectories(roomDirPath);
        } catch (IOException e) {
            throw new RuntimeException("Could not create directory for room. Error: " + e.getMessage());
        }

        for (MultipartFile video : newVideos) {
            // Tính toán hash của file video
            String fileHash = calculateFileHash(video);

            // Kiểm tra xem file có cùng hash đã tồn tại trong thư mục `room{roomId}` chưa
            String videoUrl = findExistingFileUrlByHash(roomDirPath, fileHash);

            if (videoUrl == null) {
                // Nếu video chưa tồn tại, lưu file mới
                videoUrl = fileServerService.saveVideosRoom(video, currentUser.getId(), roomId);
            }

            videoUrls.add(videoUrl);

            // Tạo đối tượng VideoRoom và liên kết với Room bằng roomId
            VideoRoom videoRoom = VideoRoom.builder()
                    .video_path(videoUrl)
                    .room(Room.builder().id(roomId).build())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            videoRoomRepository.save(videoRoom);
        }
        return videoUrls;
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
                    .map(p -> "/" + FileServerService.videoUploadDir + "/" + roomDirPath.getParent().getFileName().toString() + "/" + roomDirPath.getFileName().toString() + "/" + p.getFileName().toString())
                    .findFirst()
                    .orElse(null);
        } catch (IOException e) {
            throw new RuntimeException("Error while searching for existing file by hash. Error: " + e.getMessage());
        }
    }
}
