package com.petralib.signal.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class SignalDto {
    Long id;
    String name;
    String description;
}
