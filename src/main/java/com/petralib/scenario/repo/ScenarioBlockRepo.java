package com.petralib.scenario.repo;

import com.petralib.scenario.entity.ScenarioBlockEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScenarioBlockRepo extends JpaRepository<ScenarioBlockEntity, Long> {

    @Query("FROM ScenarioBlockEntity sbe WHERE sbe.parentWorkflow.id = :workflowId AND sbe.block.id <> :workflowId")
    Collection<ScenarioBlockEntity> findScenarioBlocksByWorkflow(@Param("workflowId") Long workflowId);

    @Query("FROM ScenarioBlockEntity sbe WHERE sbe.block.service.id = :serviceId")
    Collection<ScenarioBlockEntity> findScenarioBlocksByService(@Param("serviceId") Long serviceId);

//    @Modifying
//    @Query("DELETE FROM ScenarioBlockEntity sbe WHERE sbe.id NOT IN :ids AND sbe.parentWorkflow.id = :workflowId")
//    void deleteNotInListIds(@Param("ids") List<Long> ids, @Param("workflowId") Long workflowId);

    @Query("FROM ScenarioBlockEntity sbe WHERE sbe.block.id = :workflowId AND sbe.parentWorkflow.id = :workflowId " +
            "AND sbe.beginEnd.pointType = 'END'")
    Optional<ScenarioBlockEntity> findScenarioBlockForWorkflowExit(@Param("workflowId") Long workflowId);

    @Query("FROM ScenarioBlockEntity sbe WHERE sbe.id = :id AND sbe.varVersion = :varVersion")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<ScenarioBlockEntity> findBlockByIdAndVersionForUpdate(@Param("id") Long id, @Param("varVersion") Long version);

    @Modifying
    @Query("UPDATE ScenarioBlockEntity sbe SET sbe.varVersion = sbe.varVersion + 1 WHERE sbe.id IN :id AND sbe.varVersion = :varVersion")
    int updateVarVersion(@Param("id") Long id, @Param("varVersion") Long varVersion);
}
