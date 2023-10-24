package com.petralib.project.dto;

import com.petralib.project.entity.ProjectEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    @Mapping(source = "projectId", target = "id")
    ProjectDto entityToDto(ProjectEntity projectEntity);

    List<ProjectDto> map(List<ProjectEntity> entities);
}
