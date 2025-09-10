package com.petralib.ctype.dto;

import com.petralib.ctype.entity.CTypeEntity;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
public class CTypeShortDto {
    Long id;
    String name;
    String description;

    public CTypeShortDto(CTypeEntity entity){
        this.id = entity.getId();
        this.name = entity.getName();
        this.description = entity.getDescription();
    }
}
