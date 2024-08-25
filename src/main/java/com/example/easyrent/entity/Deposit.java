package com.example.easyrent.entity;

import com.example.easyrent.model.enums.DepositStatus;
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
@Table(name = "deposit")
public class Deposit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    int amount;

    int bonusAmount;

    int totalAmount;

    LocalDateTime createdAt;

    String vnpTransactionId;

    String paymentMethod;

    @Enumerated(EnumType.STRING)
    DepositStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @JoinColumn(name = "bonus_rule_id")
    BonusRule bonusRule;


}
