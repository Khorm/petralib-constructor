package com.petralib.scenario.repo;

import com.petralib.scenario.entity.ScenarioBlockEntity;
import com.petralib.scenario.entity.ScenarioVariableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface ScenarioVariableRepo extends JpaRepository<ScenarioVariableEntity, Long> {

    @Query("FROM ScenarioVariableEntity WHERE scenarioBlock.id = :scenarioBlockId")
    Collection<ScenarioVariableEntity> findSimpleVariables(@Param("scenarioBlockId") Long scenarioBlockId);

//    @Query("FROM ScenarioVariableEntity WHERE consumerVariable.id == :consumerVariableId AND scenarioBlock.parentWorkflow.id = :workflowId")
//    ScenarioBlockEntity findScenarioVariableByVariableAndWorkflow(@Param("consumerVariableId") Long consumerVariable,
//                                                                  @Param("workflowId") Long workflowId);
}
