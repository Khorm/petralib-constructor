package com.petralib.admin;

import com.petralib.admin.dto.AdminUserDto;
import com.petralib.admin.enums.Authority;
import com.petralib.admin.mapper.UserMapper;
import com.petralib.auth.ConstructorUserRepository;
import com.petralib.auth.security.entity.ConstructorUserEntity;
import com.petralib.auth.security.model.SecurityUser;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AdminRestController {

    ConstructorUserRepository constructorUserRepository;
    UserMapper userMapper;
    AdminService adminService;

    @GetMapping
    public Collection<AdminUserDto> getUsers() {
        List<ConstructorUserEntity> usersList = constructorUserRepository.findAll();
        for (ConstructorUserEntity entity :usersList){
            entity.setPassword("");
        }
        return userMapper.mapEntities(usersList);
    }

    @GetMapping("current-user")
    public AdminUserDto getCurrentUser(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new AuthenticationCredentialsNotFoundException("Authentication required");
        }
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        if (securityUser == null || securityUser.getUser() == null) {
            throw new AuthenticationCredentialsNotFoundException("Invalid user data");
        }
        return userMapper.entityToDto(securityUser.getUser());
    }

    @PostMapping
    public void save(Authentication authentication, @RequestBody Collection<AdminUserDto> dto){
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new AuthenticationCredentialsNotFoundException("Authentication required");
        }
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        if (securityUser == null || securityUser.getUser() == null) {
            throw new AuthenticationCredentialsNotFoundException("Invalid user data");
        }
        if (securityUser.getUser().getAuthority() != Authority.ADMIN){
            throw new AuthenticationServiceException("You are not allowed to do this");
        }
        adminService.save(dto);
    }
}
