package com.example.easyrent.controller.rest;

import com.example.easyrent.entity.ServicePrice;
import com.example.easyrent.model.enums.PriceType;
import com.example.easyrent.model.request.UpsertRoomRequest;
import com.example.easyrent.service.ImageRoomService;
import com.example.easyrent.service.RoomService;
import com.example.easyrent.service.ServicePriceService;
import com.example.easyrent.service.VideoRoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/rooms")
public class RoomApi {
    private final RoomService roomService;
    private final ImageRoomService imageRoomService;
    private final VideoRoomService videoRoomService;
    private final ServicePriceService servicePriceService;


    @PostMapping("/createRoom")
    public ResponseEntity<Integer> createRoom(@Valid @RequestBody UpsertRoomRequest request) {
        Integer roomId = roomService.createRoom(request);
        return ResponseEntity.ok(roomId);
    }

    @PostMapping("/upload-imageRoom")
    public ResponseEntity<List<String>> uploadRoomImages(@RequestParam("images") List<MultipartFile> images, @RequestParam("roomId") Integer roomId) {
        List<String> imageUrls = imageRoomService.uploadRoomImage(images, roomId);
        return ResponseEntity.ok(imageUrls);
    }

    @PostMapping("/upload-videoRoom")
    public ResponseEntity<List<String>> uploadRoomVideos(@RequestParam("videos") List<MultipartFile> videos, @RequestParam("roomId") Integer roomId) {
        List<String> videUrls = videoRoomService.uploadRoomVideo(videos, roomId);
        return ResponseEntity.ok(videUrls);
    }

}

