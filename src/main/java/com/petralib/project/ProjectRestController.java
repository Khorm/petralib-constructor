package com.petralib.project;

import com.petralib.auth.security.model.SecurityUser;
import com.petralib.project.dto.ProjectDto;
import com.petralib.project.dto.ProjectMapper;
import com.petralib.project.entity.ProjectEntity;
import com.petralib.project.service.ProjectService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/v1/project")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProjectRestController {

    ProjectService projectService;
    ProjectMapper projectMapper;


    @GetMapping
    public List<ProjectDto> getProjects(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new AuthenticationCredentialsNotFoundException("Authentication required");
        }
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        System.out.println(securityUser.getUsername());
        List<ProjectEntity> projectEntities = projectService.getProjectsForUser(securityUser.getId());
        return projectMapper.map(projectEntities);
    }


    @PostMapping
    public ResponseEntity<?> save(Authentication authentication, @Valid @RequestBody ProjectDto projectDto, Errors errors) {
        if (errors.hasErrors()) {
            Collection<String> validationErrors = errors.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
        }
        if (authentication == null || authentication.getPrincipal() == null) {
            return new ResponseEntity<>("Authentication required", HttpStatus.UNAUTHORIZED);
        }
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        if (securityUser == null || securityUser.getUser() == null) {
            return new ResponseEntity<>("Invalid user data", HttpStatus.UNAUTHORIZED);
        }

        ProjectEntity entity = projectService.save(projectDto, securityUser.getUser());
        ProjectDto answer = projectMapper.entityToDto(entity);
        return ResponseEntity.ok(answer);
    }

    @DeleteMapping("{projectId}")
    public ResponseEntity<?> delete(@PathVariable Long projectId) {
        projectService.delete(projectId);
        return ResponseEntity.ok(projectId);
    }

}
