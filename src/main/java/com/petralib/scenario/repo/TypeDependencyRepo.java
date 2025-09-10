package com.petralib.scenario.repo;

import com.petralib.scenario.entity.TypeDependenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;


@Repository
public interface TypeDependencyRepo extends JpaRepository<TypeDependenceEntity, Long> {
    @Query("FROM TypeDependenceEntity WHERE scenarioVariable.id = :scenarioVariableId")
    Collection<TypeDependenceEntity> findTypeDependenciesByScenarioValue(@Param("scenarioVariableId") Long scenarioVariableId);
}
