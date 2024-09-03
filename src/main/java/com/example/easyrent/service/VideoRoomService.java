package com.example.easyrent.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VideoRoomService {
    List<String> uploadRoomVideo(List<MultipartFile> file, Integer roomId);
}
