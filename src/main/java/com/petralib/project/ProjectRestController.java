package com.petralib.project;

import com.petralib.auth.security.model.SecurityUser;
import com.petralib.project.dto.ProjectDto;
import com.petralib.project.dto.ProjectMapper;
import com.petralib.project.entity.ProjectEntity;
import com.petralib.project.service.ProjectService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/project")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProjectRestController {

    ProjectService projectService;
    ProjectMapper projectMapper;

    @GetMapping
    public List<ProjectDto> getProjects(Authentication authentication){
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        System.out.println(securityUser.getUsername());
        List<ProjectEntity> projectEntities = projectService.getProjectsForUser(securityUser.getId());
        return projectMapper.map(projectEntities);
    }

}
