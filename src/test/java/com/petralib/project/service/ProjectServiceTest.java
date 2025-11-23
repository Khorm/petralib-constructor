package com.petralib.project.service;

import com.petralib.auth.security.entity.ConstructorUserEntity;
import com.petralib.project.dto.ProjectDto;
import com.petralib.project.entity.ProjectEntity;
import com.petralib.project.repository.ProjectRepository;
import com.petralib.test.annotation.AutoTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@AutoTest
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

    private ProjectDto projectDto;
    private ConstructorUserEntity user;

    @BeforeEach
    void setUp() {
        projectDto = new ProjectDto();
        projectDto.setName("Test Project");
        projectDto.setDescription("Test Description");

        user = new ConstructorUserEntity();
        user.setId(1L);
    }

    @Test
    void testSaveProject() {
        // Given
        when(projectRepository.save(any(ProjectEntity.class))).thenReturn(new ProjectEntity());

        // When
        ProjectEntity result = projectService.save(projectDto, user);

        // Then
        assertNotNull(result);
        verify(projectRepository).save(any(ProjectEntity.class));
    }

    @Test
    void testGetProjectsForUser() {
        // Given
        Long userId = 1L;
        when(projectRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<ProjectEntity> result = projectService.getProjectsForUser(userId);

        // Then
        assertNotNull(result);
        verify(projectRepository).findAll();
    }

    @Test
    void testDeleteProject() {
        // Given
        Long projectId = 1L;

        // When
        projectService.delete(projectId);

        // Then
        verify(projectRepository).deleteById(projectId);
    }
}

