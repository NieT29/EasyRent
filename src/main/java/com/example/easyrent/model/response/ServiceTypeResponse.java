package com.example.easyrent.model.response;

import com.example.easyrent.model.enums.PriceType;
import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ServiceTypeResponse {
    Integer id;
    String name;
    int price;
    Integer servicePriceId;
    String priceType;

    public void setPriceType(PriceType priceType) {
        this.priceType = priceType.getValue();
    }
}
