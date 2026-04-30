package com.petralib.auth.security.model;

import com.petralib.auth.security.entity.ConstructorUserEntity;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Data
public class SecurityUser implements UserDetails {

//    private final Long id;
//    private final String username;
//    private final String password;
    private final List<SimpleGrantedAuthority> authorities;
    private final boolean isActive;
    private final ConstructorUserEntity user;

    public SecurityUser(ConstructorUserEntity constructorUserEntity){
//        this.id = constructorUserEntity.getId();
//        this.username = constructorUserEntity.getEmail();
//        this.password = constructorUserEntity.getPassword();
        this.authorities = Collections.emptyList();
        this.isActive = true;
        this.user = constructorUserEntity;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();

        // 1. Добавляем роль с префиксом ROLE_ (для hasRole())
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));

        // 2. Добавляем все UserAction как отдельные authorities (для hasAuthority())
        user.getRole().getUserActions().forEach(action ->
                authorities.add(new SimpleGrantedAuthority(action.name()))
        );

        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return isActive;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActive;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isActive;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }

    public Long getId(){
        return user.getId();
    }

    public ConstructorUserEntity getUser(){
        return user;
    }
}
