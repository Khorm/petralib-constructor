package com.petralib.service.dto;

import com.petralib.block.dto.BlockDto;
import com.petralib.project.service.ProjectService;
import com.petralib.service.entity.ServiceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;

@Mapper(componentModel = "spring", uses = {ProjectService.class})
public interface ServiceMapper {

    @Mapping(target = "project", source = "projectId")
    ServiceEntity fromDtoToEntity(ServiceDto serviceDto);

    ServiceDto fromEntityToDto(ServiceEntity serviceentity);

    Collection<ServiceDto> map(Collection<ServiceEntity> serviceEntities);

    Collection<BlockDto> mapToBlock(Collection<ServiceEntity> serviceEntities);
}
