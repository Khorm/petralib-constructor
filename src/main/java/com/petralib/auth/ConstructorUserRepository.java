package com.petralib.auth;

import com.petralib.auth.security.entity.ConstructorUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConstructorUserRepository extends JpaRepository<ConstructorUserEntity, Long> {

    Optional<ConstructorUserEntity> findByEmail(String email);
}
