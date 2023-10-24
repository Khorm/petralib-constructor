package com.petralib.workflow.entity;

import com.petralib.enitity.BlockEntity;
import com.petralib.project.entity.ProjectEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.LazyCollection;

import java.util.Collection;

@Entity
@Table(name = "workflow")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class WorkflowEntity extends BlockEntity {

    @ManyToMany
    @JoinTable(name="workflow_relation", joinColumns = {@JoinColumn(name = "parent_id")},
            inverseJoinColumns = {@JoinColumn(name = "child_id")})
    Collection<WorkflowEntity> childWorkflows;

    @ManyToMany
    @JoinTable(name= "workflow_relation", joinColumns = {@JoinColumn(name = "child_id")},
            inverseJoinColumns = {@JoinColumn(name = "parent_id")})
    Collection<WorkflowEntity> parentWorkflows;

    @ManyToOne
    @JoinColumn(name = "PROJECT_ID", insertable = false, updatable = false)
    ProjectEntity project;
}
