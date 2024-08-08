package com.example.easyrent.controller;

import com.example.easyrent.entity.District;
import com.example.easyrent.entity.Room;
import com.example.easyrent.model.dto.ServiceTypeAttributesDTO;
import com.example.easyrent.model.enums.OrderServiceStatus;
import com.example.easyrent.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
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


        List<Room> roomListNew = roomService.getRoomListNew(OrderServiceStatus.ACTIVE, 1, 10).getContent();
        Page<Room> pageRoomList = roomService.getRoomsFiltered(OrderServiceStatus.ACTIVE, page, pageSize, gia_tu, gia_den, dien_tich_tu, dien_tich_den, orderBy);

        Map<String, Integer> categoryCounts = roomService.getCategoryCounts();

        List<List<Room>> allRoomLists = Arrays.asList(roomListNew, pageRoomList.getContent());
        Map<Integer, ServiceTypeAttributesDTO> roomAttributesMap = roomService.getRoomServiceTypeAttributes(allRoomLists);


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
        model.addAttribute("roomAttributesMap",roomAttributesMap);
        model.addAttribute("categoryCounts", categoryCounts);
        return "web/index";
    }

    @GetMapping({"/cho-thue-phong-tro",
            "/cho-thue-phong-tro-{province}",
            "/cho-thue-phong-tro-{province}/{district}",
            "/cho-thue-phong-tro-{province}/{district}/{ward}"})
    public String getRoomRentals(Model model,
                                 @PathVariable(required = false) String province,
                                 @PathVariable(required = false) String district,
                                 @PathVariable(required = false) String ward,
                                 @RequestParam(required = false, defaultValue = "1") int page,
                                 @RequestParam(required = false, defaultValue = "20") int pageSize,
                                 @RequestParam(required = false, defaultValue = "mac-dinh") String orderBy,
                                 @RequestParam(required = false) Integer gia_tu,
                                 @RequestParam(required = false) Integer gia_den,
                                 @RequestParam(required = false) Double dien_tich_tu,
                                 @RequestParam(required = false) Double dien_tich_den) {

        Page<Room> pageRoomList = roomService.getRoomsFiltered(province, district, ward, "Cho thuê phòng trọ", OrderServiceStatus.ACTIVE, page, pageSize, gia_tu, gia_den, dien_tich_tu, dien_tich_den, orderBy);
        List<Room> roomListNew = roomService.getRoomListNew(OrderServiceStatus.ACTIVE, 1, 10).getContent();

        Map<String, Integer> categoryCounts = roomService.getCategoryCounts();

        List<List<Room>> allRoomLists = Arrays.asList(roomListNew ,pageRoomList.getContent());
        Map<Integer, ServiceTypeAttributesDTO> roomAttributesMap = roomService.getRoomServiceTypeAttributes(allRoomLists);

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
        model.addAttribute("roomAttributesMap",roomAttributesMap);
        model.addAttribute("categoryCounts", categoryCounts);

        return "web/cho-thue-phong-tro";
    }

    @GetMapping({"/cho-thue-nha",
            "/cho-thue-nha-{province}",
            "/cho-thue-nha-{province}/{district}",
            "/cho-thue-nha-{province}/{district}/{ward}"})
    public String getHouseRentals(Model model,
                                 @PathVariable(required = false) String province,
                                 @PathVariable(required = false) String district,
                                 @PathVariable(required = false) String ward,
                                 @RequestParam(required = false, defaultValue = "1") int page,
                                 @RequestParam(required = false, defaultValue = "20") int pageSize,
                                 @RequestParam(required = false, defaultValue = "mac-dinh") String orderBy,
                                 @RequestParam(required = false) Integer gia_tu,
                                 @RequestParam(required = false) Integer gia_den,
                                 @RequestParam(required = false) Double dien_tich_tu,
                                 @RequestParam(required = false) Double dien_tich_den) {

        Page<Room> pageRoomList = roomService.getRoomsFiltered(province, district, ward, "Cho thuê nhà", OrderServiceStatus.ACTIVE, page, pageSize, gia_tu, gia_den, dien_tich_tu, dien_tich_den, orderBy);
        List<Room> roomListNew = roomService.getRoomListNew(OrderServiceStatus.ACTIVE, 1, 10).getContent();

        Map<String, Integer> categoryCounts = roomService.getCategoryCounts();

        List<List<Room>> allRoomLists = Arrays.asList(roomListNew ,pageRoomList.getContent());
        Map<Integer, ServiceTypeAttributesDTO> roomAttributesMap = roomService.getRoomServiceTypeAttributes(allRoomLists);

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
        model.addAttribute("roomAttributesMap",roomAttributesMap);
        model.addAttribute("categoryCounts", categoryCounts);

        return "web/cho-thue-nha";
    }

    @GetMapping({"/cho-thue-can-ho",
            "/cho-thue-can-ho-{province}",
            "/cho-thue-can-ho-{province}/{district}",
            "/cho-thue-can-ho-{province}/{district}/{ward}"})
    public String getApartmentRentals(Model model,
                                 @PathVariable(required = false) String province,
                                 @PathVariable(required = false) String district,
                                 @PathVariable(required = false) String ward,
                                 @RequestParam(required = false, defaultValue = "1") int page,
                                 @RequestParam(required = false, defaultValue = "20") int pageSize,
                                 @RequestParam(required = false, defaultValue = "mac-dinh") String orderBy,
                                 @RequestParam(required = false) Integer gia_tu,
                                 @RequestParam(required = false) Integer gia_den,
                                 @RequestParam(required = false) Double dien_tich_tu,
                                 @RequestParam(required = false) Double dien_tich_den) {

        Page<Room> pageRoomList = roomService.getRoomsFiltered(province, district, ward, "Cho thuê căn hộ", OrderServiceStatus.ACTIVE, page, pageSize, gia_tu, gia_den, dien_tich_tu, dien_tich_den, orderBy);
        List<Room> roomListNew = roomService.getRoomListNew(OrderServiceStatus.ACTIVE, 1, 10).getContent();

        Map<String, Integer> categoryCounts = roomService.getCategoryCounts();

        List<List<Room>> allRoomLists = Arrays.asList(roomListNew ,pageRoomList.getContent());
        Map<Integer, ServiceTypeAttributesDTO> roomAttributesMap = roomService.getRoomServiceTypeAttributes(allRoomLists);

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
        model.addAttribute("roomAttributesMap",roomAttributesMap);
        model.addAttribute("categoryCounts", categoryCounts);

        return "web/cho-thue-can-ho";
    }

    @GetMapping({"/cho-thue-mat-bang",
            "/cho-thue-mat-bang-{province}",
            "/cho-thue-mat-bang-{province}/{district}",
            "/cho-thue-mat-bang-{province}/{district}/{ward}"})
    public String getLeaseSpaces(Model model,
                                 @PathVariable(required = false) String province,
                                 @PathVariable(required = false) String district,
                                 @PathVariable(required = false) String ward,
                                 @RequestParam(required = false, defaultValue = "1") int page,
                                 @RequestParam(required = false, defaultValue = "20") int pageSize,
                                 @RequestParam(required = false, defaultValue = "mac-dinh") String orderBy,
                                 @RequestParam(required = false) Integer gia_tu,
                                 @RequestParam(required = false) Integer gia_den,
                                 @RequestParam(required = false) Double dien_tich_tu,
                                 @RequestParam(required = false) Double dien_tich_den) {

        Page<Room> pageRoomList = roomService.getRoomsFiltered(province, district, ward, "Cho thuê mặt bằng", OrderServiceStatus.ACTIVE, page, pageSize, gia_tu, gia_den, dien_tich_tu, dien_tich_den, orderBy);
        List<Room> roomListNew = roomService.getRoomListNew(OrderServiceStatus.ACTIVE, 1, 10).getContent();

        Map<String, Integer> categoryCounts = roomService.getCategoryCounts();

        List<List<Room>> allRoomLists = Arrays.asList(roomListNew ,pageRoomList.getContent());
        Map<Integer, ServiceTypeAttributesDTO> roomAttributesMap = roomService.getRoomServiceTypeAttributes(allRoomLists);

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
        model.addAttribute("roomAttributesMap",roomAttributesMap);
        model.addAttribute("categoryCounts", categoryCounts);

        return "web/cho-thue-mat-bang";
    }

    @GetMapping({"/tim-nguoi-o-ghep",
            "/tim-nguoi-o-ghep-{province}",
            "/tim-nguoi-o-ghep-{province}/{district}",
            "/tim-nguoi-o-ghep-{province}/{district}/{ward}"})
    public String getRoommateSearch(Model model,
                                 @PathVariable(required = false) String province,
                                 @PathVariable(required = false) String district,
                                 @PathVariable(required = false) String ward,
                                 @RequestParam(required = false, defaultValue = "1") int page,
                                 @RequestParam(required = false, defaultValue = "20") int pageSize,
                                 @RequestParam(required = false, defaultValue = "mac-dinh") String orderBy,
                                 @RequestParam(required = false) Integer gia_tu,
                                 @RequestParam(required = false) Integer gia_den,
                                 @RequestParam(required = false) Double dien_tich_tu,
                                 @RequestParam(required = false) Double dien_tich_den) {

        Page<Room> pageRoomList = roomService.getRoomsFiltered(province, district, ward, "Tìm người ở ghép", OrderServiceStatus.ACTIVE, page, pageSize, gia_tu, gia_den, dien_tich_tu, dien_tich_den, orderBy);
        List<Room> roomListNew = roomService.getRoomListNew(OrderServiceStatus.ACTIVE, 1, 10).getContent();

        Map<String, Integer> categoryCounts = roomService.getCategoryCounts();

        List<List<Room>> allRoomLists = Arrays.asList(roomListNew ,pageRoomList.getContent());
        Map<Integer, ServiceTypeAttributesDTO> roomAttributesMap = roomService.getRoomServiceTypeAttributes(allRoomLists);

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
        model.addAttribute("roomAttributesMap",roomAttributesMap);
        model.addAttribute("categoryCounts", categoryCounts);

        return "web/tim-nguoi-o-ghep";
    }

    @GetMapping("/posts/{id}/{slug}")
    public String getDetailRoom(Model model, @PathVariable("id") int id, @PathVariable("slug") String slug) {
        Room room = roomService.getRoom(id, slug, OrderServiceStatus.ACTIVE);
        List<Room> roomListNew = roomService.getRoomListNewExcludingCurrent(OrderServiceStatus.ACTIVE, id ,1, 10).getContent();
        List<Room> relateRooms = roomService.getRelateRooms(OrderServiceStatus.ACTIVE, id);
        Map<District, Integer> roomsInSameProvince = roomService.getCountRoomsByDistrictCategoryAndStatus(room.getCategory().getId(), room.getDistrict().getProvince().getId(), OrderServiceStatus.ACTIVE);

        List<List<Room>> allRoomLists = Arrays.asList(roomListNew, relateRooms);
        Map<Integer, ServiceTypeAttributesDTO> roomAttributesMap = roomService.getRoomServiceTypeAttributes(allRoomLists, room);



        model.addAttribute("room", room);
        model.addAttribute("roomListNew", roomListNew);
        model.addAttribute("relateRooms", relateRooms);
        model.addAttribute("roomsInSameProvince", roomsInSameProvince);
        model.addAttribute("roomAttributesMap", roomAttributesMap);
        return "web/detail-room";
    }
}