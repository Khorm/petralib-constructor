package com.petralib.project.entity;

import com.petralib.workflow.entity.WorkflowEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Table(name = "PROJECTS")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
public class ProjectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PROJECT_ID", nullable = false)
    Long projectId;

    @Column(name = "project_name", nullable = false)
    String name;
    String description;

    @OneToMany(mappedBy = "project")
    List<ProjectUserRolesEntity> userRoles;


}
