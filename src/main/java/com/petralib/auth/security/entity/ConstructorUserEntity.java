package com.petralib.auth.security.entity;

import com.petralib.auth.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;import java.util.Collection;
import java.util.List;


@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "constructor_users")
@NoArgsConstructor@AllArgsConstructor
public class ConstructorUserEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    Long id;

    String email;

    @Column(name = "user_name", unique = true, nullable = false)
    String name;

    @Column(name = "user_password", nullable = false)
    String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    Role role;

    // Превращаем Роль и её Actions в список GrantedAuthority для Spring Security
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 1. Добавляем саму роль (с префиксом ROLE_)
        List<SimpleGrantedAuthority> authorities = new ArrayList<>(List.of(
                new SimpleGrantedAuthority("ROLE_" + role.name())
        ));

        // 2. Добавляем все экшены роли как отдельные права (Authorities)
        authorities.addAll(role.getUserActions().stream()
                .map(action -> new SimpleGrantedAuthority(action.name()))
                .toList());

        return authorities;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
