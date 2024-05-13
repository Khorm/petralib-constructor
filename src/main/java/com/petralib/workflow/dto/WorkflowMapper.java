package com.petralib.workflow.dto;

import com.petralib.variable.VariableMapper;
import com.petralib.workflow.entity.WorkflowEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WorkflowMapper {

    WorkflowCollectionObjectDto entityToWorkflowPageObj(WorkflowEntity workflow);

    List<WorkflowCollectionObjectDto> map(List<WorkflowEntity> entities);


    WorkflowEntity fromDtoToEntity(WorkflowDto workflowDto);
}
