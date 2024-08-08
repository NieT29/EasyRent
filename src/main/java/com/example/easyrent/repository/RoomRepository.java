package com.example.easyrent.repository;

import com.example.easyrent.entity.Category;
import com.example.easyrent.entity.Province;
import com.example.easyrent.entity.Room;
import com.example.easyrent.model.enums.OrderServiceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Integer> {
    @Query("SELECT r FROM Room r " +
            "JOIN r.orderServices os " +
            "WHERE os.status = :status " +
            "ORDER BY r.createdAt DESC")
    Page<Room> findByOrderServiceStatusOrderByCreatedAtDesc(@Param("status") OrderServiceStatus status, Pageable pageable);

    @Query("SELECT r FROM Room r " +
            "JOIN r.orderServices os " +
            "WHERE os.status = :status " +
            "AND r.id != :id " +
            "ORDER BY r.createdAt DESC")
    Page<Room> findByOrderServiceStatusAndIdNotOrderByCreatedAtDesc(@Param("status") OrderServiceStatus status,
                                                                    @Param("id") Integer id,
                                                                    Pageable pageable);


    @Query("SELECT r FROM Room r " +
            "JOIN r.orderServices os " +
            "WHERE os.status = :status " +
            "AND (:gia_tu IS NULL OR r.price >= :gia_tu) " +
            "AND (:gia_den IS NULL OR r.price <= :gia_den) " +
            "AND (:dien_tich_tu IS NULL OR r.acreage >= :dien_tich_tu) " +
            "AND (:dien_tich_den IS NULL OR r.acreage <= :dien_tich_den) " +
            "ORDER BY r.createdAt DESC")
    Page<Room> findAllByOrderServiceStatusAndPriceAndAcreageOrderByCreatedAtDesc(@Param("status") OrderServiceStatus status,
                                                                                 @Param("gia_tu") Integer gia_tu,
                                                                                 @Param("gia_den") Integer gia_den,
                                                                                 @Param("dien_tich_tu") Double dien_tich_tu,
                                                                                 @Param("dien_tich_den") Double dien_tich_den,
                                                                                 Pageable pageable);

    @Query("SELECT r FROM Room r " +
            "JOIN r.orderServices os " +
            "WHERE os.status = :status " +
            "AND r.videoRooms IS NOT EMPTY " +
            "AND (:gia_tu IS NULL OR r.price >= :gia_tu) " +
            "AND (:gia_den IS NULL OR r.price <= :gia_den) " +
            "AND (:dien_tich_tu IS NULL OR r.acreage >= :dien_tich_tu) " +
            "AND (:dien_tich_den IS NULL OR r.acreage <= :dien_tich_den) " +
            "ORDER BY r.createdAt DESC")
    Page<Room> findAllByOrderServiceStatusAndHasVideoAndPriceAndAcreage(@Param("status") OrderServiceStatus status,
                                                                        @Param("gia_tu") Integer gia_tu,
                                                                        @Param("gia_den") Integer gia_den,
                                                                        @Param("dien_tich_tu") Double dien_tich_tu,
                                                                        @Param("dien_tich_den") Double dien_tich_den,
                                                                        Pageable pageable);


    @Query("SELECT r FROM Room r " +
            "JOIN OrderService os ON r.id = os.room.id " +
            "JOIN os.serviceType st " +
            "WHERE os.status = :status " +
            "AND (:gia_tu IS NULL OR r.price >= :gia_tu) " +
            "AND (:gia_den IS NULL OR r.price <= :gia_den) " +
            "AND (:dien_tich_tu IS NULL OR r.acreage >= :dien_tich_tu) " +
            "AND (:dien_tich_den IS NULL OR r.acreage <= :dien_tich_den) " +
            "ORDER BY st.priority ASC, r.createdAt DESC")
    Page<Room> findAllByOrderServiceStatusAndPriceAndAcreage(@Param("status") OrderServiceStatus status,
                                                             @Param("gia_tu") Integer gia_tu,
                                                             @Param("gia_den") Integer gia_den,
                                                             @Param("dien_tich_tu") Double dien_tich_tu,
                                                             @Param("dien_tich_den") Double dien_tich_den,
                                                             Pageable pageable);


    @Query("SELECT r FROM Room r " +
            "JOIN r.orderServices os " +
            "JOIN r.ward w " +
            "JOIN w.district d " +
            "JOIN d.province p " +
            "JOIN r.category c " +
            "WHERE os.status = :status " +
            "AND c.name = :categoryName " +
            "AND (:province IS NULL OR TRIM(p.name) = :province) " +
            "AND (:district IS NULL OR TRIM(d.name) = :district) " +
            "AND (:ward IS NULL OR TRIM(w.name) = :ward) " +
            "AND (:gia_tu IS NULL OR r.price >= :gia_tu) " +
            "AND (:gia_den IS NULL OR r.price <= :gia_den) " +
            "AND (:dien_tich_tu IS NULL OR r.acreage >= :dien_tich_tu) " +
            "AND (:dien_tich_den IS NULL OR r.acreage <= :dien_tich_den) " +
            "ORDER BY r.createdAt DESC")
    Page<Room> findAllByLocationAndCategoryAndOrderServiceStatusAndPriceAndAcreageOrderByCreatedAtDesc(@Param("province") String province,
                                                                                                       @Param("district") String district,
                                                                                                       @Param("ward") String ward,
                                                                                                       @Param("categoryName") String categoryName,
                                                                                                       @Param("status") OrderServiceStatus status,
                                                                                                       @Param("gia_tu") Integer gia_tu,
                                                                                                       @Param("gia_den") Integer gia_den,
                                                                                                       @Param("dien_tich_tu") Double dien_tich_tu,
                                                                                                       @Param("dien_tich_den") Double dien_tich_den,
                                                                                                       Pageable pageable);



    @Query("SELECT r FROM Room r " +
            "JOIN r.orderServices os " +
            "JOIN r.ward w " +
            "JOIN w.district d " +
            "JOIN d.province p " +
            "JOIN r.category c " +
            "WHERE os.status = :status " +
            "AND r.videoRooms IS NOT EMPTY " +
            "AND (:province IS NULL OR :province = '' OR p.name = :province) " +
            "AND (:district IS NULL OR :district = '' OR d.name = :district) " +
            "AND (:ward IS NULL OR :ward = '' OR w.name = :ward) " +
            "AND c.name = :categoryName " +
            "AND (:gia_tu IS NULL OR r.price >= :gia_tu) " +
            "AND (:gia_den IS NULL OR r.price <= :gia_den) " +
            "AND (:dien_tich_tu IS NULL OR r.acreage >= :dien_tich_tu) " +
            "AND (:dien_tich_den IS NULL OR r.acreage <= :dien_tich_den) " +
            "ORDER BY r.createdAt DESC")
    Page<Room> findAllByOrderServiceStatusAndLocationAndCategoryAndHasVideoAndPriceAndAcreage(@Param("province") String province,
                                                                                              @Param("district") String district,
                                                                                              @Param("ward") String ward,
                                                                                              @Param("categoryName") String categoryName,
                                                                                              @Param("status") OrderServiceStatus status,
                                                                                              @Param("gia_tu") Integer gia_tu,
                                                                                              @Param("gia_den") Integer gia_den,
                                                                                              @Param("dien_tich_tu") Double dien_tich_tu,
                                                                                              @Param("dien_tich_den") Double dien_tich_den,
                                                                                              Pageable pageable);

    @Query("SELECT r FROM Room r " +
            "JOIN r.orderServices os " +
            "JOIN r.ward w " +
            "JOIN w.district d " +
            "JOIN d.province p " +
            "JOIN os.serviceType st " +
            "JOIN r.category c " +
            "WHERE os.status = :status " +
            "AND (:province IS NULL OR :province = '' OR p.name = :province) " +
            "AND (:district IS NULL OR :district = '' OR d.name = :district) " +
            "AND (:ward IS NULL OR :ward = '' OR w.name = :ward) " +
            "AND c.name = :categoryName " +
            "AND (:gia_tu IS NULL OR r.price >= :gia_tu) " +
            "AND (:gia_den IS NULL OR r.price <= :gia_den) " +
            "AND (:dien_tich_tu IS NULL OR r.acreage >= :dien_tich_tu) " +
            "AND (:dien_tich_den IS NULL OR r.acreage <= :dien_tich_den) " +
            "ORDER BY st.priority ASC, r.createdAt DESC")
    Page<Room> findAllByOrderServiceStatusAndLocationAndCategoryAndPriceAndAcreage(@Param("province") String province,
                                                                                   @Param("district") String district,
                                                                                   @Param("ward") String ward,
                                                                                   @Param("categoryName") String categoryName,
                                                                                   @Param("status") OrderServiceStatus status,
                                                                                   @Param("gia_tu") Integer gia_tu,
                                                                                   @Param("gia_den") Integer gia_den,
                                                                                   @Param("dien_tich_tu") Double dien_tich_tu,
                                                                                   @Param("dien_tich_den") Double dien_tich_den,
                                                                                   Pageable pageable);




    @Query("SELECT r FROM Room r " +
            "JOIN r.orderServices os " +
            "WHERE r.id = :id " +
            "AND r.slug = :slug " +
            "AND os.status = :status")
    Room findByIdAndSlugAndOrderServiceStatus(@Param("id") Integer id,
                                              @Param("slug") String slug,
                                              @Param("status") OrderServiceStatus status);


    @Query("SELECT r FROM Room r " +
            "JOIN r.orderServices os " +
            "WHERE r.category = (SELECT rr.category FROM Room rr WHERE rr.id = :roomId) " +
            "AND r.district = (SELECT rr.district FROM Room rr WHERE rr.id = :roomId) " +
            "AND os.status = :status " +
            "AND r.id != :roomId " +
            "ORDER BY r.createdAt DESC")
    List<Room> findRelateRooms(@Param("roomId") Integer roomId,
                               @Param("status") OrderServiceStatus status);


    @Query("SELECT r.district, COUNT(r) FROM Room r " +
            "JOIN r.orderServices os " +
            "WHERE r.category.id = :categoryId " +
            "AND r.district.province.id = :provinceId " +
            "AND os.status = :status " +
            "GROUP BY r.district")
    List<Object[]> countRoomsByDistrictAndCategoryAndStatus(@Param("categoryId") Integer categoryId,
                                                            @Param("provinceId") Integer provinceId,
                                                            @Param("status") OrderServiceStatus status);


    @Query("SELECT COUNT(r) FROM Room r " +
            "JOIN r.orderServices os " +
            "WHERE r.category.name = :categoryName AND os.status = 'ACTIVE'")
    Integer countByCategoryNameAndActiveOrderService(@Param("categoryName") String categoryName);


}
