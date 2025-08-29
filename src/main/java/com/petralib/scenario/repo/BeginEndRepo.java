package com.petralib.scenario.repo;

import com.petralib.scenario.entity.BeginEndEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BeginEndRepo extends JpaRepository<BeginEndEntity, Long> {

    @Query("FROM BeginEndEntity bee WHERE bee.workflow.id = :workflowId")
    List<BeginEndEntity> getStartEndByWorkflow(@Param("workflowId") Long workflowId);

    @Query("FROM BeginEndEntity bee WHERE bee.workflow.id = :workflowId AND bee.pointType = 'END'")
    BeginEndEntity getEndByWorkflow(@Param("workflowId") Long workflowId);

}
