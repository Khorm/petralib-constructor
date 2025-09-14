package com.petralib.ctype.dto;

import com.petralib.ctype.entity.CTypeEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CTypeShortDto {
    Long id;
    String name;
    String description;

}
