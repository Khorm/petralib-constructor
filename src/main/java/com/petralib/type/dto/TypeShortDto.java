package com.petralib.type.dto;

import com.petralib.type.entity.TypeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
public class TypeShortDto {
    Long id;
    String name;
    String description;

    public TypeShortDto(TypeEntity entity){
        this.id = entity.getId();
        this.name = entity.getName();
        this.description = entity.getDescription();
    }
}
