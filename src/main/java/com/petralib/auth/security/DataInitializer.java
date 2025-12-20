package com.petralib.auth.security;

import com.petralib.auth.ConstructorUserRepository;
import com.petralib.auth.Role;
import com.petralib.auth.security.entity.ConstructorUserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final ConstructorUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.password}")
    private String adminPassword;

    @Override
    public void run(String... args) {
        if (userRepository.findByName(adminUsername).isEmpty()) {
            ConstructorUserEntity admin = ConstructorUserEntity.builder()
                    .name(adminUsername)
                    .password(passwordEncoder.encode(adminPassword))
                    .role(Role.ADMIN)
                    .build();
            userRepository.save(admin);
            System.out.println("Admin user initialized in database.");
        }
    }
}
