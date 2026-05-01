package com.petralib.admin;

import com.petralib.admin.dto.AdminUserDto;
import com.petralib.admin.enums.Authority;
import com.petralib.admin.mapper.UserMapper;
import com.petralib.auth.ConstructorUserRepository;
import com.petralib.auth.security.entity.ConstructorUserEntity;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AdminService {
    ConstructorUserRepository constructorUserRepository;
    PasswordEncoder passwordEncoder;
    UserMapper userMapper;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void onApplicationStartup() {
        if (constructorUserRepository.findByEmail("admin@petra.com").isEmpty()) {
            ConstructorUserEntity admin = new ConstructorUserEntity();
            admin.setEmail("admin@petra.com");
            admin.setName("Admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setAuthority(Authority.ADMIN);
            constructorUserRepository.save(admin);
        }
    }

    @Transactional
    public void save(Collection<AdminUserDto> users) {
        for (AdminUserDto user : users) {
            if (user.getChanged() && !user.getDeleted()) {
                ConstructorUserEntity entity = userMapper.dtoToEntity(user);
                if (user.getPassword() == null || user.getPassword().isBlank()) {
                    if (user.getId() != null) {
                        ConstructorUserEntity oldPasswordHolder = constructorUserRepository.findById(user.getId()).orElseThrow();
                        entity.setPassword(oldPasswordHolder.getPassword());
                    } else {
                        throw new NullPointerException("No password provided");
                    }
                } else {
                    entity.setPassword(passwordEncoder.encode(user.getPassword()));
                }
                constructorUserRepository.save(entity);
            }
            if (user.getDeleted() && user.getId() != null) {
                constructorUserRepository.deleteById(user.getId());
            }
        }
    }

}
