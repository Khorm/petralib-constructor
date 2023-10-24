package com.petralib.workflow.entity;

import com.petralib.auth.Role;
import com.petralib.auth.security.entity.ConstructorUserEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "WORKFLOW_CONSTRAINS")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class WorkflowUserConstraintEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CONSTRAINT_ID", nullable = false)
    Long workflowId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_ID", insertable = false, updatable = false)
    ConstructorUserEntity constructorUserEntity;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "WORKFLOW_ID", insertable = false, updatable = false)
    WorkflowEntity workflow;

    @Enumerated(EnumType.STRING)
    Role role;
}
