package com.petralib.workflow.dto;

import com.petralib.workflow.entity.WorkflowEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WorkflowMapper {

    WorkflowCollectionObjectDto EntityToWorkflowPageObj(WorkflowEntity workflow);

    List<WorkflowCollectionObjectDto> map(List<WorkflowEntity> entities);
}
