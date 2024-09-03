package com.example.easyrent.model.request;

import com.example.easyrent.entity.Category;
import com.example.easyrent.entity.District;
import com.example.easyrent.entity.Province;
import com.example.easyrent.entity.Ward;
import com.example.easyrent.model.enums.SubjectRent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpsertRoomRequest {
    @NotEmpty(message = "Tiêu đề không được để trống")
    @Size(max = 100, message = "Tiêu đề không được dài quá 100 ký tự")
    String title;

    @NotEmpty(message = "Mô tả không được để trống")
    String description;

    SubjectRent subjectRent;

    @NotNull(message = "Giá cho thuê không được để trống")
    int price;

    @NotNull(message = "Diện tích không được để trống")
    Double acreage;

    @NotNull(message = "Danh mục cho thuê không được để trống")
    Integer categoryId;

    @NotNull(message = "Tỉnh/Thành Phố không được để trống")
    Integer provinceId;

    @NotNull(message = "Quận/Huyện không được để trống")
    Integer districtId;

    @NotNull(message = "Phường/Xã không được để trống")
    Integer wardId;

    @NotNull(message = "Số nhà, tên đường/phố không được để trống")
    String streetDetail;

    String exactAddress;
}
