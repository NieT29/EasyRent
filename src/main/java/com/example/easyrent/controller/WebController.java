package com.example.easyrent.controller;

import com.example.easyrent.entity.District;
import com.example.easyrent.entity.Room;
import com.example.easyrent.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class WebController {
    private final RoomService roomService;

    @GetMapping("/")
    public String getHome(Model model,
                          @RequestParam(required = false, defaultValue = "1") int page,
                          @RequestParam(required = false, defaultValue = "20") int pageSize,
                          @RequestParam(required = false, defaultValue = "mac-dinh") String orderBy,
                          @RequestParam(required = false) Integer gia_tu,
                          @RequestParam(required = false) Integer gia_den,
                          @RequestParam(required = false) Double dien_tich_tu,
                          @RequestParam(required = false) Double dien_tich_den) {


        List<Room> roomListNew = roomService.getRoomListNew(true, 1, 10).getContent();
        Page<Room> pageRoomList;


        pageRoomList = roomService.getAllRoomsFiltered(true, page, pageSize, gia_tu, gia_den, dien_tich_tu, dien_tich_den, orderBy);


        int totalPages = pageRoomList.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = roomService.getPageNumbers(page, totalPages);
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("pageRoomList", pageRoomList);
        model.addAttribute("orderBy", orderBy);
        model.addAttribute("currentPage", page);
        model.addAttribute("roomListNew", roomListNew);
        model.addAttribute("gia_tu", gia_tu);
        model.addAttribute("gia_den", gia_den);
        model.addAttribute("dien_tich_tu", dien_tich_tu);
        model.addAttribute("dien_tich_den", dien_tich_den);
        return "web/index";
    }

    @GetMapping("/posts/{id}/{slug}")
    public String getDetailRoom(Model model, @PathVariable("id") int id, @PathVariable("slug") String slug) {
        Room room = roomService.getRoom(id, slug, true);
        List<Room> roomListNew = roomService.getRoomListNewExcludingCurrent(true,id ,1, 10).getContent();
        List<Room> relateRooms = roomService.getRelateRooms(true, id);
        Map<District, Integer> roomsInSameProvince = roomService.getCountRoomsByDistrictCategoryAndStatus(room.getCategory().getId(), room.getDistrict().getProvince().getId(), true);

        model.addAttribute("room", room);
        model.addAttribute("roomListNew", roomListNew);
        model.addAttribute("relateRooms", relateRooms);
        model.addAttribute("roomsInSameProvince", roomsInSameProvince);
        return "web/detail-room";
    }
}