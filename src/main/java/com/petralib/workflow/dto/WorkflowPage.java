package com.petralib.workflow.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Getter
@Deprecated
public class WorkflowPage {
    Integer pageCount;
    Long allObjectsCount;
    List<WorkflowCollectionObjectDto> workflowCollectionObjectDtos;
}
