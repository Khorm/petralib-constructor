package com.petralib.block;

import com.petralib.block.enitity.BlockEntity;
import com.petralib.variable.VariableMapper;
import com.petralib.workflow.dto.WorkflowDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = VariableMapper.class)
public interface BlockMapper {

    @Mapping(source = "variableList", target = "variables")
    BlockEntity fromWorkflowDtoToEntity(WorkflowDto workflowDto);


    @Mapping(source = "variables", target = "variableList")
    @Mapping(source = "project.projectId", target = "projectId")
    WorkflowDto fromEntityToWorkflowDto(BlockEntity blockEntity);
}
