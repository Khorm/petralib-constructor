package com.petralib.workflow.repository;

import com.petralib.workflow.entity.WorkflowEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkflowRepository extends JpaRepository<WorkflowEntity, Long> {
    @Query("FROM WorkflowEntity we WHERE we.project.projectId = :projectId AND we.parentWorkflows IS EMPTY")
    Page<WorkflowEntity> findHighLevelWorkflows(@Param("projectId") Long projectId, Pageable pageable);

    @Query("SELECT we.childWorkflows FROM WorkflowEntity we WHERE we.id = :parentId")
    List<WorkflowEntity> findWorkflowsByParent(@Param("parentId") Long parentId);

    @Query("FROM WorkflowEntity we WHERE we.project.projectId = :projectId")
    Page<WorkflowEntity> findWorkflows(@Param("projectId") Long projectId, Pageable pageable);

    @Query("FROM WorkflowEntity we WHERE we.project.projectId = :projectId AND lower(we.name) like" +
            " lower(CONCAT('%', :workflowName, '%'))")
    Page<WorkflowEntity> findWorkflowsByName(@Param("projectId") Long projectId,
                                             @Param("workflowName") String workflowName, Pageable pageable);
}
