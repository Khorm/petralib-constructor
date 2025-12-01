package com.petralib.project.dto;

import com.petralib.auth.security.model.SecurityUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "user.name", target = "name")
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "user.id", target = "id")
    UserDto securityUserToDto(SecurityUser securityUser);
}
