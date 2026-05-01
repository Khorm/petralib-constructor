package com.petralib.admin.mapper;

import com.petralib.admin.dto.AdminUserDto;
import com.petralib.auth.security.entity.ConstructorUserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "email", target = "userEmail")
    @Mapping(source = "name", target = "userName")
    @Mapping(source = "authority", target = "role")
    AdminUserDto entityToDto(ConstructorUserEntity userEntity);


    List<AdminUserDto> mapEntities(List<ConstructorUserEntity> entities);


    @Mapping(source = "userEmail", target = "email")
    @Mapping(source = "userName", target = "name")
    @Mapping(source = "role", target = "authority")
    ConstructorUserEntity dtoToEntity(AdminUserDto userDto);

    List<ConstructorUserEntity> mapDtos(List<AdminUserDto> dtos);
}
