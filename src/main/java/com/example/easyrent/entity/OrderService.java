package com.example.easyrent.entity;

import com.example.easyrent.model.enums.OrderServiceStatus;
import com.example.easyrent.model.enums.PaymentMethod;
import com.example.easyrent.model.enums.PaymentStatus;
import com.example.easyrent.model.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "orderService")
public class OrderService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    TransactionType transactionType;

    @Column(nullable = false)
    int totalDay;

    @Column(nullable = false)
    int totalPrice;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    OrderServiceStatus status;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    PaymentStatus paymentStatus;

    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    PaymentMethod paymentMethod;

    LocalDateTime orderDate;
    LocalDateTime startDate;
    LocalDateTime endDate;

    @ManyToOne
    @JoinColumn(name = "room_id")
    Room room;

    @ManyToOne
    @JoinColumn(name = "serviceType_id")
    ServiceType serviceType;

    @ManyToOne
    @JoinColumn(name = "service_price_id")
    ServicePrice servicePrice;
}
