package com.petralib.workflow.entity;

import com.petralib.block.enitity.BlockEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Collection;

//@Entity
//@Table(name = "workflow")
//@PrimaryKeyJoinColumn(name = "workflow_id")
//@FieldDefaults(level = AccessLevel.PRIVATE)
//@Getter
//@Setter
public class WorkflowEntity  {

//    @ManyToMany
//    @JoinTable(name="workflow_relation", joinColumns = {@JoinColumn(name = "parent_id")},
//            inverseJoinColumns = {@JoinColumn(name = "child_id")})
//    Collection<WorkflowEntity> childWorkflows;
//
//    @ManyToMany
//    @JoinTable(name= "workflow_relation", joinColumns = {@JoinColumn(name = "child_id")},
//            inverseJoinColumns = {@JoinColumn(name = "parent_id")})
//    Collection<WorkflowEntity> parentWorkflows;
}
