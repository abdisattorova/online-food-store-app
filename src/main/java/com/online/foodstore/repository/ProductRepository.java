package com.online.foodstore.repository;

import com.online.foodstore.model.entity.Product;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends BaseRepository<Product> {

    Integer countAllByCategoryId(Long categoryId);
}
