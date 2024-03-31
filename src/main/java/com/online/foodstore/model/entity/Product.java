package com.online.foodstore.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "products")
public class Product extends BaseEntity {
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private Category category;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private EProductStatus status;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "expire_date")
    private LocalDate expireDate;
}
