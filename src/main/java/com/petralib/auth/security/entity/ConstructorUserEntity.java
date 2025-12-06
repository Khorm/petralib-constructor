package com.petralib.auth.security.entity;

import com.petralib.auth.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;


@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "constructor_users")
public class ConstructorUserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    Long id;

    String email;

    @Column(name = "user_name")
    String name;

    @Column(name = "user_password")
    String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
}
