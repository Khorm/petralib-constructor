package com.petralib.project.service;

import com.petralib.auth.UserAction;
import com.petralib.project.entity.ProjectEntity;
import com.petralib.project.repository.ProjectRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ProjectService {

    ProjectRepository projectRepository;

    @Transactional(readOnly = true)
    public List<ProjectEntity> getProjectsForUser(Long userId) {
        return projectRepository.getProjectsByUser(userId);
    }

    @Transactional(readOnly = true)
    public boolean isUserAcceptTo(Long userId, Long projectId, UserAction userAction) {
        Optional<ProjectEntity> projectEntityOptional = projectRepository.findById(projectId);
        if (projectEntityOptional.isPresent()) {
            return projectEntityOptional.get().getUserRoles().stream()
                    .filter(userRole -> userRole.getUser().getId().equals(userId))
                    .anyMatch(userRole -> userRole.getRole().isActionAccepted(userAction));
        }
        throw new NullPointerException("Project not found");
    }

}
