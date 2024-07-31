package com.example.easyrent.service;

import com.example.easyrent.entity.Category;
import com.example.easyrent.entity.District;
import com.example.easyrent.entity.Province;
import com.example.easyrent.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Map;

public interface RoomService {
//    Page<Room> getAllRoomsByServiceType(Boolean status, int page, int pageSize );
//
//    Page<Room> getAllRoomsSortedByCreatedAtDesc(Boolean status, int page, int pageSize);
//
//    Page<Room> getAllRoomsWithVideo(Boolean status, int page, int pageSize);

    List<Integer> getPageNumbers(int currentPage, int totalPages);

    Page<Room> getRoomListNew(Boolean status, int page, int pageSize);
    Page<Room> getRoomListNewExcludingCurrent(Boolean status,Integer currentRoomId  ,int page, int pageSize);

    Page<Room> getAllRoomsFiltered(Boolean status, int page, int pageSize, Integer gia_tu, Integer gia_den, Double dien_tich_tu, Double dien_tich_den, String orderBy);


    Room getRoom(Integer id, String slug, Boolean status);


    List<Room> getRelateRooms(Boolean status, Integer roomId);

    Map<District, Integer> getCountRoomsByDistrictCategoryAndStatus(Integer categoryId, Integer provinceId, Boolean status);

}
