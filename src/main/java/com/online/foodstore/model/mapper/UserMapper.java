package com.online.foodstore.model.mapper;

import com.online.foodstore.model.dto.UserDTO;
import com.online.foodstore.model.entity.User;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface UserMapper extends GenericMapper<User, UserDTO> {
}
