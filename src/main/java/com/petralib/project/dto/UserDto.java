package com.petralib.project.dto;

import com.petralib.auth.Role;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    String name;
    String email;
    String password;
    Role role;
}
