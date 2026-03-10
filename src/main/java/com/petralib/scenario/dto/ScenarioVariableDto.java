package com.petralib.scenario.dto;

import com.petralib.ctype.dto.CTypeFieldDto;
import com.petralib.ctype.entity.CTypeFieldEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.sql.ast.tree.expression.Collation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class ScenarioVariableDto {
    Long scenarioVariableId;
    String type;
    String functionType;
    Long consumerVariableId;
    Long producerId;
    List<CTypeFieldDto> typeInheritance = new ArrayList<>();
    String script;
    Long blockVariableId;
}
