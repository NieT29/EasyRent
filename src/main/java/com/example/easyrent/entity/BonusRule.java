package com.example.easyrent.entity;

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
@Table(name = "bonus_rule")
public class BonusRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    int minAmount;

    int maxAmount;

    int bonusPercentage;

    String description;
}
