package com.example.easyrent.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageRoomService  {
    List<String> uploadRoomImage(List<MultipartFile> file, Integer roomId);
}
