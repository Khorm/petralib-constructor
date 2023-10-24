package com.petralib.auth.security.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "constructor_user")
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


}
