package com.example.easyrent;

import com.example.easyrent.model.enums.OrderServiceStatus;
import com.example.easyrent.model.enums.PriceType;
import com.example.easyrent.utils.RandomColorUtils;
import com.example.easyrent.entity.*;
import com.example.easyrent.model.enums.SubjectRent;
import com.example.easyrent.model.enums.UserRole;
import com.example.easyrent.repository.*;
import com.github.javafaker.Faker;
import com.github.slugify.Slugify;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@SpringBootTest
class EasyRentApplicationTests {

    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ImageRoomRepository imageRoomRepository;
    @Autowired
    private VideoRoomRepository videoRoomRepository;
    @Autowired
    private ServiceTypeRepository serviceTypeRepository;
    @Autowired
    private ProvinceRepository provinceRepository;
    @Autowired
    private DistrictRepository districtRepository;
    @Autowired
    private WardRepository wardRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ServicePriceRepository servicePriceRepository;
    @Autowired
    private OrderServiceRepository orderServiceRepository;


    @Test
    void save_room() {
        Random random = new Random();
        Faker faker = new Faker();
        Slugify slugify = Slugify.builder().build();

        List<Category> categorieList = categoryRepository.findAll();
        List<Province> provinceList = provinceRepository.findAll();
        List<User> userList = userRepository.findAll();


        for (int i = 0; i < 1000 ; i++) {
            Category categoryRd = categorieList.get(random.nextInt(categorieList.size()));

            Province provinceRd = provinceList.get(random.nextInt(provinceList.size()));

            List<District> districtsInProvince = districtRepository.findByProvince(provinceRd);
            District districtRd = districtsInProvince.get(random.nextInt(districtsInProvince.size()));

            List<Ward> wardsInDistrict = wardRepository.findByDistrict(districtRd);
            Ward wardRd = wardsInDistrict.get(random.nextInt(wardsInDistrict.size()));

            User userRd = userList.get(random.nextInt(userList.size()));

            int price = (random.nextInt(171) + 10) * 100000;
            double acreage = 10 + random.nextInt(120);

            String streetDetail = faker.address().streetAddress();
            String exactAddress = streetDetail + ", " + wardRd.getName() + ", " + districtRd.getName() + ", " + provinceRd.getName();

            String name = faker.book().title();
            Room room = Room.builder()
                    .title(name)
                    .slug(slugify.slugify(name))
                    .description(faker.lorem().paragraph())
                    .subjectRent(SubjectRent.values()[random.nextInt(SubjectRent.values().length)])
                    .price(price)
                    .acreage(acreage)
                    .streetDetail(streetDetail)
                    .exactAddress(exactAddress)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .expiresAt(LocalDateTime.now().plusDays(7))
                    .user(userRd)
                    .category(categoryRd)
                    .province(provinceRd)
                    .district(districtRd)
                    .ward(wardRd)
                    .build();
            roomRepository.save(room);
        }
    }

    @Test
    void seve_user() {
        Faker faker = new Faker();
        Random random = new Random();

        for (int i = 0; i < 50; i++) {
            int accountBalance = (random.nextInt(91) + 10) * 1000;
            String name = faker.name().fullName();
            User user = User.builder()
                    .name(name)
                    .email(faker.internet().emailAddress())
                    .avatar("https://placehold.co/600x400?text=" + String.valueOf(name.charAt(0)).toUpperCase())
                    .password("123")
                    .role(i == 0 || i == 1 ? UserRole.ADMIN : UserRole.USER)
                    .phoneNumber("0943648763")
                    .accountBalance(accountBalance)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            userRepository.save(user);
        }
    }

    @Test
    void save_service_price() {
        ServiceType normalService = serviceTypeRepository.findByName("Tin thường");
        ServiceType vipHighlightService = serviceTypeRepository.findByName("Tin VIP nổi bật");
        ServiceType vipService = serviceTypeRepository.findByName("Tin VIP");

        // giá tin thường
        servicePriceRepository.save(ServicePrice.builder()
                .priceType(PriceType.DAILY)
                .price(2000)
                .discountPrice(2000)
                .serviceType(normalService)
                .build());

        servicePriceRepository.save(ServicePrice.builder()
                .priceType(PriceType.WEEKLY)
                .price(12000)
                .discountPrice(12000)
                .serviceType(normalService)
                .build());

        servicePriceRepository.save(ServicePrice.builder()
                .priceType(PriceType.MONTHLY)
                .price(60000)
                .discountPrice(48000)
                .serviceType(normalService)
                .build());

        // giá tin VIP
        servicePriceRepository.save(ServicePrice.builder()
                .priceType(PriceType.DAILY)
                .price(30000)
                .discountPrice(30000)
                .serviceType(vipService)
                .build());

        servicePriceRepository.save(ServicePrice.builder()
                .priceType(PriceType.WEEKLY)
                .price(190000)
                .discountPrice(190000)
                .serviceType(vipService)
                .build());

        servicePriceRepository.save(ServicePrice.builder()
                .priceType(PriceType.MONTHLY)
                .price(900000)
                .discountPrice(800000)
                .serviceType(vipService)
                .build());

        // giá tin VIP nổi bật
        servicePriceRepository.save(ServicePrice.builder()
                .priceType(PriceType.DAILY)
                .price(50000)
                .discountPrice(50000)
                .serviceType(vipHighlightService)
                .build());

        servicePriceRepository.save(ServicePrice.builder()
                .priceType(PriceType.WEEKLY)
                .price(315000)
                .discountPrice(315000)
                .serviceType(vipHighlightService)
                .build());

        servicePriceRepository.save(ServicePrice.builder()
                .priceType(PriceType.MONTHLY)
                .price(1500000)
                .discountPrice(1200000)
                .serviceType(vipHighlightService)
                .build());
    }

    @Test
    void save_order() {
        Random random = new Random();

        List<Room> roomList = roomRepository.findAll();
        List<ServiceType> serviceTypeList = serviceTypeRepository.findAll();
        List<ServicePrice> servicePriceList = servicePriceRepository.findAll();

        OrderServiceStatus[] statuses = {OrderServiceStatus.ACTIVE, OrderServiceStatus.PENDING_APPROVAL, OrderServiceStatus.EXPIRED};

        for (Room room : roomList) {
            ServiceType serviceTypeRd = serviceTypeList.get(random.nextInt(serviceTypeList.size()));

            List<ServicePrice> filteredServicePrices = servicePriceList.stream()
                    .filter(sp -> sp.getServiceType().getId().equals(serviceTypeRd.getId()))
                    .toList();


            ServicePrice servicePriceRd = filteredServicePrices.get(random.nextInt(filteredServicePrices.size()));

            int totalDay = random.nextInt(30) + 1;

            int totalPrice = totalDay * servicePriceRd.getDiscountPrice();


            LocalDateTime orderDate = room.getUpdatedAt();
            LocalDateTime startDate = orderDate;
            LocalDateTime endDate = startDate.plusDays(totalDay);

            OrderServiceStatus status = statuses[random.nextInt(statuses.length)];


            OrderService orderService = OrderService.builder()
                    .totalDay(totalDay)
                    .totalPrice(totalPrice)
                    .orderDate(room.getUpdatedAt())
                    .startDate(orderDate)
                    .endDate(endDate)
                    .room(room)
                    .serviceType(serviceTypeRd)
                    .servicePrice(servicePriceRd)
                    .status(status)
                    .build();

            orderServiceRepository.save(orderService);
        }
    }

    @Test
    void save_imageRoom() {
        Random random = new Random();
        List<Room> roomList = roomRepository.findAll();

        for (Room room : roomList) {
            int numberOfImages = random.nextInt(3) + 3; // Số lượng hình ảnh sẽ là 3, 4 hoặc 5
            for (int i = 0; i < numberOfImages; i++) {
                String color = RandomColorUtils.getRandomColor();
                ImageRoom imageRoom = ImageRoom.builder()
                        .img_path("https://placehold.co/600x400/"+color+ "/FFF" + "?text=" + String.valueOf(room.getTitle().charAt(0)).toUpperCase())
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .room(room)
                        .build();
                imageRoomRepository.save(imageRoom);
            }
        }
    }

    @Test
    void save_videoRoom() {
        Faker faker = new Faker();
        Random random = new Random();
        List<Room> roomList = roomRepository.findAll();

        for (Room room : roomList) {
            if (random.nextInt(3) == 0) {
                int numberOfVideos = random.nextInt(2) + 1;
                for (int i = 0; i < numberOfVideos; i++) {
                    VideoRoom videoRoom = VideoRoom.builder()
                            .video_path("https://www.example.com/video" + faker.number().digits(5))
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .room(room)
                            .build();
                    videoRoomRepository.save(videoRoom);
                }
            }
        }
    }

}
