package com.petralib.service.dto;

import com.petralib.block.dto.BlockDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Collection;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Data
public class ServicePage {
    Integer pageCount;
    Long allObjectsCount;
    Collection<ServiceDto> blocks;
}
