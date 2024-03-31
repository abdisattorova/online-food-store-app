package com.online.foodstore.model.mapper;

import com.online.foodstore.model.dto.ProductDTO;
import com.online.foodstore.model.dto.UserDTO;
import com.online.foodstore.model.entity.Product;
import com.online.foodstore.model.entity.User;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface ProductMapper extends GenericMapper<Product, ProductDTO> {
    @Override
    @Mappings({
            @Mapping(target = "categoryName", source = "category.name")
    })
    ProductDTO toDto(Product entity);

    @Override
    @Mappings({
            @Mapping(target = "categoryName", source = "category.name")
    })
    List<ProductDTO> toDtoList(List<Product> entities);
}
