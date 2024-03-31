package com.online.foodstore.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "categories")
public class Category extends BaseEntity {
    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private ECategoryStatus status;
}
