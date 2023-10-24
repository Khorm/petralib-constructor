package com.petralib.auth.security;

import com.petralib.auth.security.entity.ConstructorUserEntity;
import com.petralib.auth.ConstructorUserRepository;
import com.petralib.auth.security.model.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConstructorUserDetailsService implements UserDetailsService {

    private final ConstructorUserRepository constructorUserRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        ConstructorUserEntity user = constructorUserRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("User doesn't exist"));
        return new SecurityUser(user);
    }
}
