package com.example.easyrent.service;


import com.example.easyrent.entity.District;
import com.example.easyrent.entity.Room;
import com.example.easyrent.model.dto.ServiceTypeAttributesDTO;
import com.example.easyrent.model.enums.OrderServiceStatus;
import com.example.easyrent.model.request.UpsertRoomRequest;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface RoomService {
    List<Integer> getPageNumbers(int currentPage, int totalPages);

    Page<Room> getRoomListNew(OrderServiceStatus status, int page, int pageSize);

    Page<Room> getRoomListNewExcludingCurrent(OrderServiceStatus status ,Integer currentRoomId  ,int page, int pageSize);

    Page<Room> getRoomsFiltered(OrderServiceStatus status, int page, int pageSize, Integer gia_tu, Integer gia_den, Double dien_tich_tu, Double dien_tich_den, String orderBy);

    Room getRoom(Integer id, String slug, OrderServiceStatus status);

    List<Room> getRelateRooms(OrderServiceStatus status, Integer roomId);

    Map<District, Integer> getCountRoomsByDistrictCategoryAndStatus(Integer categoryId, Integer provinceId, OrderServiceStatus status);

    Map<Integer, ServiceTypeAttributesDTO> getRoomServiceTypeAttributes(List<List<Room>> roomLists, Room currentRoom);

    Map<Integer, ServiceTypeAttributesDTO> getRoomServiceTypeAttributes(List<List<Room>> roomLists);

    Page<Room> getRoomsFiltered(String province, String district, String ward, String categoryName ,OrderServiceStatus status, int page, int pageSize, Integer gia_tu, Integer gia_den, Double dien_tich_tu, Double dien_tich_den, String orderBy);

    Map<String, Integer> getCategoryCounts();

    Integer createRoom(UpsertRoomRequest request);

    Room getRoomById(Integer id);
}
