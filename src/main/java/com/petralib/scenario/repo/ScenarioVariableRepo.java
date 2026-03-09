package com.petralib.scenario.repo;

import com.petralib.scenario.entity.ScenarioBlockEntity;
import com.petralib.scenario.entity.ScenarioVariableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Optional;

public interface ScenarioVariableRepo extends JpaRepository<ScenarioVariableEntity, Long> {

    @Query("FROM ScenarioVariableEntity WHERE scenarioBlock.id = :scenarioBlockId")
    Collection<ScenarioVariableEntity> findVariables(@Param("scenarioBlockId") Long scenarioBlockId);

    @Modifying
    @Query("DELETE FROM ScenarioVariableEntity sve WHERE sve.producerVariable.id = :variableId OR sve.consumerVariable.id = :variableId")
    void deleteByVariable(@Param("variableId") Long variableId);

}
