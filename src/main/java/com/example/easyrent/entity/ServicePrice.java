package com.example.easyrent.entity;

import com.example.easyrent.model.enums.PriceType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "servicePrice")
public class ServicePrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Enumerated(EnumType.STRING)
    PriceType priceType;

    @Column(nullable = false)
    int price;

    @Column(nullable = false)
    int discountPrice;

    @ManyToOne
    @JoinColumn(name = "serviceType_id")
     ServiceType serviceType;
}
