package com.example.easyrent.repository;

import com.example.easyrent.entity.Category;
import com.example.easyrent.entity.Province;
import com.example.easyrent.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Integer> {
    Page<Room> findByStatusOrderByCreatedAtDesc(Boolean status, Pageable pageable);
    Page<Room> findByStatusAndIdNotOrderByCreatedAtDesc(Boolean status, Integer id, Pageable pageable);



    @Query("SELECT r FROM Room r WHERE r.status = :status " +
            "AND (:gia_tu IS NULL OR r.price >= :gia_tu) " +
            "AND (:gia_den IS NULL OR r.price <= :gia_den) " +
            "AND (:dien_tich_tu IS NULL OR r.acreage >= :dien_tich_tu) " +
            "AND (:dien_tich_den IS NULL OR r.acreage <= :dien_tich_den) " +
            "ORDER BY r.createdAt DESC")
    Page<Room> findAllByStatusAndPriceAndAcreageOrderByCreatedAtDesc(Boolean status,
                                                                     @Param("gia_tu") Integer gia_tu,
                                                                     @Param("gia_den") Integer gia_den,
                                                                     @Param("dien_tich_tu") Double dien_tich_tu,
                                                                     @Param("dien_tich_den") Double dien_tich_den,
                                                                     Pageable pageable);

    @Query("SELECT r FROM Room r WHERE r.status = :status AND r.videoRooms IS NOT EMPTY " +
            "AND (:gia_tu IS NULL OR r.price >= :gia_tu) " +
            "AND (:gia_den IS NULL OR r.price <= :gia_den) " +
            "AND (:dien_tich_tu IS NULL OR r.acreage >= :dien_tich_tu) " +
            "AND (:dien_tich_den IS NULL OR r.acreage <= :dien_tich_den) " +
            "ORDER BY r.createdAt DESC")
    Page<Room> findAllByStatusAndHasVideoAndPriceAndAcreage(Boolean status,
                                                            @Param("gia_tu") Integer gia_tu,
                                                            @Param("gia_den") Integer gia_den,
                                                            @Param("dien_tich_tu") Double dien_tich_tu,
                                                            @Param("dien_tich_den") Double dien_tich_den,
                                                            Pageable pageable);

    @Query("SELECT r FROM Room r WHERE r.status = :status " +
            "AND (:gia_tu IS NULL OR r.price >= :gia_tu) " +
            "AND (:gia_den IS NULL OR r.price <= :gia_den) " +
            "AND (:dien_tich_tu IS NULL OR r.acreage >= :dien_tich_tu) " +
            "AND (:dien_tich_den IS NULL OR r.acreage <= :dien_tich_den) " +
            "ORDER BY r.serviceType.priority ASC, r.createdAt DESC")
    Page<Room> findAllByStatusAndPriceAndAcreage(Boolean status,
                                                 @Param("gia_tu") Integer gia_tu,
                                                 @Param("gia_den") Integer gia_den,
                                                 @Param("dien_tich_tu") Double dien_tich_tu,
                                                 @Param("dien_tich_den") Double dien_tich_den,
                                                 Pageable pageable);

    Room findByIdAndSlugAndStatus(Integer id, String slug, Boolean status);

    @Query("SELECT r FROM Room r WHERE r.category = (SELECT rr.category FROM Room rr WHERE rr.id = :roomId) " +
            "AND r.district = (SELECT rr.district FROM Room rr WHERE rr.id = :roomId) " +
            "AND r.status = :status " +
            "AND r.id != :roomId ORDER BY r.createdAt DESC")
    List<Room> findRelateRooms(@Param("roomId") Integer roomId, @Param("status") Boolean status);

    @Query("SELECT r.district, COUNT(r) FROM Room r WHERE r.category.id = :categoryId AND r.district.province.id = :provinceId AND r.status = :status GROUP BY r.district")
    List<Object[]> countRoomsByDistrictAndCategoryAndStatus(@Param("categoryId") Integer categoryId, @Param("provinceId") Integer provinceId, @Param("status") Boolean status);



}
