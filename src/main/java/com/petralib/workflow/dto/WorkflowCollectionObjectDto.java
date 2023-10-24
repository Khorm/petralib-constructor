package com.petralib.workflow.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class WorkflowCollectionObjectDto {
    Long id;
    String name;
}
