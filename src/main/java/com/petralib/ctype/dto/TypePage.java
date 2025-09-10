package com.petralib.ctype.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Collection;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Data
public class TypePage {
    Integer pageCount;
    Collection<TypeFullDto> blocks;
}
