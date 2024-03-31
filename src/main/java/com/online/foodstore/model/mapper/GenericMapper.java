package com.online.foodstore.model.mapper;

import com.online.foodstore.model.dto.BaseDTO;
import com.online.foodstore.model.entity.BaseEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

public interface GenericMapper<E extends BaseEntity, M extends BaseDTO> {
    E toEntity(M dto);

    M toDto(E entity);

    List<M> toDtoList(List<E> entities);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(@MappingTarget E entity, M model);
}

