package com.petralib.auth.rest;

import com.petralib.auth.ConstructorUserRepository;
import com.petralib.auth.Role;
import com.petralib.auth.security.ConstructorUserDetailsService;
import com.petralib.auth.security.entity.ConstructorUserEntity;
import com.petralib.project.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final PasswordEncoder passwordEncoder;
    private final ConstructorUserRepository repository;
    private final ConstructorUserDetailsService userDetailsService;

    // 1. Создание нового пользователя
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> createUser(@RequestBody UserDto userDto) {
        if (userDetailsService.loadUserByUsername(userDto.getName()).isEnabled()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
        }
        String userName = userDto.getName();
        String userPass = userDto.getPassword();
        Role userRole = userDto.getRole() != null ? userDto.getRole() : Role.USER;

        ConstructorUserEntity entity = ConstructorUserEntity.builder()
                .name(userName)
                .password(passwordEncoder.encode(userPass))
                .role(userRole)
                .build();
        repository.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
    }

    // 2. Получение списка всех пользователей
    @GetMapping("/get-users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ConstructorUserEntity>> getAllUsers() {
        List<ConstructorUserEntity> users = repository.findAll();
        return ResponseEntity.ok(users);
    }

    // 3. Обновление пароля пользователя
    @PutMapping("/{username}/password")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updatePassword(@PathVariable String username, @RequestBody String newPassword) {
        Optional<ConstructorUserEntity> entityOptional = repository.findByName(username);
        if (entityOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ConstructorUserEntity entity = entityOptional.get();
        entity.setPassword(newPassword);
        repository.save(entity);
        return ResponseEntity.ok("Password updated for user: " + username);
    }

    // 4. Удаление пользователя
    @DeleteMapping("/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable String username) {
        Optional<ConstructorUserEntity> entityOptional = repository.findByName(username);
        if (entityOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        repository.delete(entityOptional.get());
        return ResponseEntity.ok("User deleted: " + username);
    }

    @GetMapping("/info")
    public ResponseEntity<String> info() {
        return ResponseEntity.ok("User management system is active. Admin can create/delete users.");
    }
}
