package com.petralib.project.entity;

import com.petralib.auth.security.entity.ConstructorUserEntity;
import com.petralib.workflow.entity.WorkflowEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.*;
import java.util.function.Predicate;

@Entity
@Table(name = "PROJECTS")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class ProjectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PROJECT_ID", nullable = false)
    Long projectId;

    @Column(name = "project_name", nullable = false)
    String name;
    String description;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    List<ProjectUserRolesEntity> userRoles = new ArrayList<>();

    public Optional<ProjectUserRolesEntity> addUserRole(ConstructorUserEntity user){
        ProjectUserRolesEntity userRolesEntity = new ProjectUserRolesEntity();
        userRolesEntity.setProject(this);
        userRolesEntity.setUser(user);
        if (userRoles.stream().noneMatch(userRoles -> userRoles.getUser().getId().equals(user.getId()))){
            userRoles.add(userRolesEntity);
            return Optional.of(userRolesEntity);
        }
        return Optional.empty();
    }

}
