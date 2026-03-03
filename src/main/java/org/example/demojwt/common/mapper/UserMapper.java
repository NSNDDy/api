package org.example.demojwt.common.mapper;

import org.example.demojwt.common.dto.UserDto;
import org.example.demojwt.info.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "token", ignore = true)
    @Mapping(target = "role", ignore = true)
    User toEntity(UserDto dto);
}

