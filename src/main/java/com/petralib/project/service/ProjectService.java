package com.petralib.project.service;

import com.petralib.auth.UserAction;
import com.petralib.auth.security.entity.ConstructorUserEntity;
import com.petralib.auth.security.model.SecurityUser;
import com.petralib.project.dto.ProjectDto;
import com.petralib.project.entity.ProjectEntity;
import com.petralib.project.repository.ProjectRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ProjectService {

    ProjectRepository projectRepository;

    @Transactional(readOnly = true)
    public List<ProjectEntity> getProjectsForUser(Long userId) {
//        return projectRepository.getProjectsByUser(userId);
        return projectRepository.findAll();
    }

    @Transactional(readOnly = true)
    public ProjectEntity getProject(Long projectId){
        Optional<ProjectEntity> project = projectRepository.findById(projectId);
        if (project.isPresent()){
            return project.get();
        }else {
            throw new NullPointerException("Project not found");
        }

    }

//    @Transactional(readOnly = true)
//    public boolean isUserAcceptTo(Long userId, Long projectId, UserAction userAction) {
//        Optional<ProjectEntity> projectEntityOptional = projectRepository.findById(projectId);
//        if (projectEntityOptional.isPresent()) {
//            return projectEntityOptional.get().getUserRoles().stream()
//                    .filter(userRole -> userRole.getUser().getId().equals(userId))
//                    .anyMatch(userRole -> userRole.getRole().isActionAccepted(userAction));
//        }
//        throw new NullPointerException("Project not found");
//    }

    @Transactional
    public ProjectEntity save(ProjectDto projectDto, ConstructorUserEntity user){
        ProjectEntity project = Optional.ofNullable(projectDto.getId())
                .flatMap(projectRepository::findById)
                .orElse(new ProjectEntity());
        project.setName(projectDto.getName());
        project.setDescription(projectDto.getDescription());
//        project.addUserRole(user);
        return projectRepository.save(project);
    }

    @Transactional
    public void delete(Long projectId){
        projectRepository.deleteById(projectId);
    }

}
