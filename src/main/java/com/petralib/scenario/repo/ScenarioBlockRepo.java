package com.petralib.scenario.repo;

import com.petralib.scenario.entity.ScenarioBlockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScenarioBlockRepo extends JpaRepository<ScenarioBlockEntity, Long> {

    @Query("FROM ScenarioBlockEntity sbe WHERE sbe.parentWorkflow.id = :workflowId")
    List<ScenarioBlockEntity> findScenarioBlocksByWorkflow(@Param("workflowId") Long workflowId);

    @Query("FROM ScenarioBlockEntity sbe WHERE sbe.block.service.id = :serviceId")
    List<ScenarioBlockEntity> findScenarioBlocksByService(@Param("serviceId") Long serviceId);

//    @Modifying
//    @Query("DELETE FROM ScenarioBlockEntity sbe WHERE sbe.id NOT IN :ids AND sbe.parentWorkflow.id = :workflowId")
//    void deleteNotInListIds(@Param("ids") List<Long> ids, @Param("workflowId") Long workflowId);

    @Query("FROM ScenarioBlockEntity sbe WHERE sbe.block.id = :workflowId AND sbe.parentWorkflow.id = :workflowId")
    Optional<ScenarioBlockEntity> findScenarioBlockForWorkflowExit(@Param("workflowId") Long workflowId);
}
