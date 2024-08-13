package com.example.easyrent.service.Impl;

import com.example.easyrent.entity.*;
import com.example.easyrent.model.dto.ServiceTypeAttributesDTO;
import com.example.easyrent.model.enums.OrderServiceStatus;
import com.example.easyrent.repository.RoomRepository;
import com.example.easyrent.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;


    @Override
    public Page<Room> getRoomsFiltered(OrderServiceStatus status, int page, int pageSize, Integer gia_tu, Integer gia_den, Double dien_tich_tu, Double dien_tich_den, String orderBy) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        if (orderBy.equals("moi-nhat")) {
            return roomRepository.findAllByOrderServiceStatusAndPriceAndAcreageOrderByStartDateDesc(status, gia_tu, gia_den, dien_tich_tu, dien_tich_den, pageRequest);
        } else if (orderBy.equals("co-video")) {
            return roomRepository.findAllByOrderServiceStatusAndHasVideoAndPriceAndAcreage(status, gia_tu, gia_den, dien_tich_tu, dien_tich_den, pageRequest);
        } else {
            return roomRepository.findAllByOrderServiceStatusAndPriceAndAcreage(status, gia_tu, gia_den, dien_tich_tu, dien_tich_den, pageRequest);
        }
    }

    @Override
    public Page<Room> getRoomsFiltered(String province, String district, String ward, String categoryName , OrderServiceStatus status, int page, int pageSize, Integer gia_tu, Integer gia_den, Double dien_tich_tu, Double dien_tich_den, String orderBy) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);

        if (orderBy.equals("moi-nhat")) {
            return roomRepository.findAllByLocationAndCategoryAndOrderServiceStatusAndPriceAndAcreageOrderByStartDateDesc(province, district, ward, categoryName, status, gia_tu, gia_den, dien_tich_tu, dien_tich_den, pageRequest);
        } else if (orderBy.equals("co-video")) {
            return roomRepository.findAllByOrderServiceStatusAndLocationAndCategoryAndHasVideoAndPriceAndAcreage(province, district, ward, categoryName, status, gia_tu, gia_den, dien_tich_tu, dien_tich_den, pageRequest);
        } else {
            return roomRepository.findAllByOrderServiceStatusAndLocationAndCategoryAndPriceAndAcreage(province, district, ward, categoryName ,status, gia_tu, gia_den, dien_tich_tu, dien_tich_den, pageRequest);
        }
    }

    @Override
    public Map<String, Integer> getCategoryCounts() {
        Map<String, Integer> categoryCounts = new HashMap<>();


        List<String> categories = Arrays.asList("Cho thuê phòng trọ",
                "Cho thuê nhà",
                "Cho thuê căn hộ",
                "Cho thuê mặt bằng",
                "Tìm người ở ghép");

        for (String category : categories) {
            Integer count = roomRepository.countByCategoryNameAndActiveOrderService(category);
            categoryCounts.put(category, count);
        }

        return categoryCounts;
    }

    @Override
    public Room getRoom(Integer id, String slug, OrderServiceStatus status) {
        return roomRepository.findByIdAndSlugAndOrderServiceStatus(id, slug, status);
    }

    @Override
    public List<Room> getRelateRooms(OrderServiceStatus status, Integer roomId) {
        return roomRepository.findRelateRooms(roomId, status).stream()
                .limit(10)
                .toList();
    }

    @Override
    public Map<District, Integer> getCountRoomsByDistrictCategoryAndStatus(Integer categoryId, Integer provinceId, OrderServiceStatus status) {
        List<Object[]> results = roomRepository.countRoomsByDistrictAndCategoryAndStatus(categoryId, provinceId, status);
        Map<District, Integer> countMap = new HashMap<>();
        for (Object[] result : results) {
            District district = (District) result[0];
            Integer count = ((Number) result[1]).intValue();
            countMap.put(district, count);

        }
        return countMap;
    }

    @Override
    public Map<Integer, ServiceTypeAttributesDTO> getRoomServiceTypeAttributes(List<List<Room>> roomLists, Room currentRoom) {
        Map<Integer, ServiceTypeAttributesDTO> roomAttributesMap = new HashMap<>();

        for (List<Room> rooms : roomLists) {
            for (Room room : rooms) {
                if (room != null) {
                    addRoomToAttributesMap(room, roomAttributesMap);
                }
            }
        }

        if (currentRoom != null) {
            addRoomToAttributesMap(currentRoom, roomAttributesMap);
        }

        return roomAttributesMap;
    }

    public Map<Integer, ServiceTypeAttributesDTO> getRoomServiceTypeAttributes(List<List<Room>> roomLists) {
        return getRoomServiceTypeAttributes(roomLists, null);
    }

    private void addRoomToAttributesMap(Room room, Map<Integer, ServiceTypeAttributesDTO> roomAttributesMap) {
        if (room == null) {
            return;
        }


        List<OrderService> orderServices = room.getOrderServices();
        if (orderServices.isEmpty()) {
            return;
        }

        OrderService activeOrderService = orderServices.get(0);
        ServiceType serviceType = activeOrderService.getServiceType();

        if (serviceType == null) {
            return;
        }

        ServiceTypeAttributesDTO attributesDTO = new ServiceTypeAttributesDTO(
                serviceType.getName(),
                serviceType.getPriority(),
                serviceType.isShowPhone(),
                serviceType.getTitleColor()
        );

        roomAttributesMap.put(room.getId(), attributesDTO);
    }

    @Override
    public List<Integer> getPageNumbers(int currentPage, int totalPages) {
        List<Integer> pageNumbers = new ArrayList<>();
        int start = Math.max(1, currentPage - 2);
        int end = Math.min(totalPages, currentPage + 2);

        if (start > 1) {
            pageNumbers.add(1);
            if (start > 2) {
                pageNumbers.add(-1);
            }
        }

        for (int i = start; i <= end; i++) {
            pageNumbers.add(i);
        }

        if (end < totalPages) {
            if (end < totalPages - 1) {
                pageNumbers.add(-1);
            }
            pageNumbers.add(totalPages);
        }

        return pageNumbers;
    }

    @Override
    public Page<Room> getRoomListNew(OrderServiceStatus status, int page, int pageSize) {
        PageRequest pageRequest = PageRequest.of(page -1, pageSize);
        return roomRepository.findByOrderServiceStatusOrderByStartDateDesc(status, pageRequest);
    }

    @Override
    public Page<Room> getRoomListNewExcludingCurrent(OrderServiceStatus status, Integer currentRoomId, int page, int pageSize) {
        PageRequest pageRequest = PageRequest.of(page -1, pageSize);
        return roomRepository.findByOrderServiceStatusAndIdNotOrderByStartDateDesc(status, currentRoomId, pageRequest);
    }
}
