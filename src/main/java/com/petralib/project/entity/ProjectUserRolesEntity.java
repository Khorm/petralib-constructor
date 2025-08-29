package com.petralib.project.entity;

import com.petralib.auth.Role;
import com.petralib.auth.security.entity.ConstructorUserEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "project_user_authorities")
@Data
public class ProjectUserRolesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auth_id", nullable = false)
    Long id;

    @ManyToOne
    @JoinColumn(name = "AUTH_USER_ID")
    ConstructorUserEntity user;
    @ManyToOne
    @JoinColumn(name = "AUTH_PROJECT_ID")
    ProjectEntity project;

//    @Enumerated(EnumType.STRING)
//    @Column(name = "project_authority", nullable = false)
//    Role role;


}
