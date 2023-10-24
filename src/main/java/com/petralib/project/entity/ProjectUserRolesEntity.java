package com.petralib.project.entity;

import com.petralib.auth.Role;
import com.petralib.auth.security.entity.ConstructorUserEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "project_user_roles")
@Getter
public class ProjectUserRolesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auth_id", nullable = false)
    Long id;

    @ManyToOne
    @JoinColumn(name = "AUTH_USER_ID", insertable = false, updatable = false)
    ConstructorUserEntity user;
    @ManyToOne
    @JoinColumn(name = "AUTH_PROJECT_ID", insertable = false, updatable = false)
    ProjectEntity project;

    @Enumerated(EnumType.STRING)
    @Column(name = "PROJECT_ROLE", nullable = false)
    Role role;


}
