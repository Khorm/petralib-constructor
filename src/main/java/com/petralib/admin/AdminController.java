package com.petralib.admin;

import com.petralib.admin.enums.Authority;
import com.petralib.auth.security.model.SecurityUser;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
//@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
//@RequiredArgsConstructor
public class AdminController {

    @GetMapping
    public String getAdmin(Authentication authentication){
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new AuthenticationCredentialsNotFoundException("Authentication required");
        }
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        if (securityUser == null || securityUser.getUser() == null) {
            throw new AuthenticationCredentialsNotFoundException("Invalid user data");
        }

        if (securityUser.getUser().getAuthority() != Authority.ADMIN){
            throw new AuthenticationServiceException("Only admins!!");
        }

        return "admin";
    }
}
