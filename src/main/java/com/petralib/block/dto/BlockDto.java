package com.petralib.block.dto;

import com.petralib.block.enums.BlockType;
import com.petralib.service.dto.ServiceDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class BlockDto {
    Long id;
    @NotEmpty(message = "Block name is empty")
    @Size(max = 100, message = "Block name is too long")
    String name;
    String description;

    @NotNull(message = "Block type is empty")
    BlockType type;

    @NotNull(message = "Service is empty")
    ServiceDto service;

    @Valid
    List<VariableDto> variables;
}
