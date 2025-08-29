package com.petralib.service.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class ServiceDto {
    Long id;

    @NotEmpty(message = "Service name is empty")
    @Size(max = 100, message = "Service name is too long")
    String name;

    @NotNull(message = "Project id is null")
    Long projectId;
    String description;

    @NotEmpty(message = "Path is empty")
    @Size(max = 100, message = "Path is too long")
    String path;
}
