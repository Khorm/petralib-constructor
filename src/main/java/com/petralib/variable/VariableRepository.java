package com.petralib.variable;

import com.petralib.variable.entity.VariableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface VariableRepository extends JpaRepository<VariableEntity, Long> {

    @Query("FROM VariableEntity where block.id = :blockId")
    Collection<VariableEntity> findVariablesForBlock(@Param("blockId") Long blockId);
}
