package com.petralib.admin.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class AdminUserDto {
    Long id;
    String userEmail;
    String userName;
    String password;
    String role;
    Boolean changed = false;
    Boolean deleted = false;
}
