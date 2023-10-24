package com.petralib.workflow.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Getter
public class WorkflowPage {
    Integer pageCount;
    Long allObjectsCount;
    List<WorkflowCollectionObjectDto> workflowCollectionObjectDtos;
}
