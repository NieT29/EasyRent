package com.example.easyrent.service.Impl;

import com.example.easyrent.entity.Category;
import com.example.easyrent.entity.District;
import com.example.easyrent.entity.Province;
import com.example.easyrent.entity.Room;
import com.example.easyrent.repository.RoomRepository;
import com.example.easyrent.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;

    @Override
    public Page<Room> getAllRoomsFiltered(Boolean status, int page, int pageSize, Integer gia_tu, Integer gia_den, Double dien_tich_tu, Double dien_tich_den, String orderBy) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        if (orderBy.equals("moi-nhat")) {
            return roomRepository.findAllByStatusAndPriceAndAcreageOrderByCreatedAtDesc(status, gia_tu, gia_den, dien_tich_tu, dien_tich_den, pageRequest);
        } else if (orderBy.equals("co-video")) {
            return roomRepository.findAllByStatusAndHasVideoAndPriceAndAcreage(status, gia_tu, gia_den, dien_tich_tu, dien_tich_den, pageRequest);
        } else {
            return roomRepository.findAllByStatusAndPriceAndAcreage(status, gia_tu, gia_den, dien_tich_tu, dien_tich_den, pageRequest);
        }
    }

    @Override
    public Room getRoom(Integer id, String slug, Boolean status) {
        return roomRepository.findByIdAndSlugAndStatus(id, slug, status);
    }

    @Override
    public List<Room> getRelateRooms(Boolean status, Integer roomId) {
        return roomRepository.findRelateRooms(roomId, status).stream()
                .limit(10)
                .toList();
    }

    @Override
    public Map<District, Integer> getCountRoomsByDistrictCategoryAndStatus(Integer categoryId, Integer provinceId, Boolean status) {
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
    public Page<Room> getRoomListNew(Boolean status, int page, int pageSize) {
        PageRequest pageRequest = PageRequest.of(page -1, pageSize);
        return roomRepository.findByStatusOrderByCreatedAtDesc(status, pageRequest);
    }

    @Override
    public Page<Room> getRoomListNewExcludingCurrent(Boolean status, Integer currentRoomId, int page, int pageSize) {
        PageRequest pageRequest = PageRequest.of(page -1, pageSize);
        return roomRepository.findByStatusAndIdNotOrderByCreatedAtDesc(status, currentRoomId, pageRequest);
    }

//    @Override
//    public Page<Room> getAllRoomsByServiceType(Boolean status, int page, int pageSize) {
//        PageRequest pageRequest = PageRequest.of(page -1, pageSize);
//        return roomRepository.findAllByStatusOrderByServiceType_PriorityAscCreatedAtDesc(true, pageRequest);
//    }
//
//    @Override
//    public Page<Room> getAllRoomsSortedByCreatedAtDesc(Boolean status, int page, int pageSize) {
//        PageRequest pageRequest = PageRequest.of(page -1, pageSize);
//        return roomRepository.findByStatusOrderByCreatedAtDesc(true, pageRequest);
//    }
//
//    @Override
//    public Page<Room> getAllRoomsWithVideo(Boolean status, int page, int pageSize) {
//        PageRequest pageRequest = PageRequest.of(page -1, pageSize);
//        return roomRepository.findAllByStatusAndHasVideoRoomsOrderByCreatedAtDesc(true, pageRequest);
//    }

}
