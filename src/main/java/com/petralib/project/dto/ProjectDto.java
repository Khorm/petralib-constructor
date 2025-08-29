package com.petralib.project.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectDto {
    Long id;

    @NotEmpty(message = "Block name is empty")
    @Size(max = 100, message = "Block name is too long")
    String name;
    String description;
}
