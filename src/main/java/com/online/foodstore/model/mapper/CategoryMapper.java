package com.online.foodstore.model.mapper;

import com.online.foodstore.model.dto.CategoryDTO;
import com.online.foodstore.model.entity.Category;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface CategoryMapper extends GenericMapper<Category, CategoryDTO> {
}
