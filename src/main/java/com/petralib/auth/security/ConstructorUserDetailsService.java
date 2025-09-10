package com.petralib.auth.security;

import com.petralib.auth.security.entity.ConstructorUserEntity;
import com.petralib.auth.ConstructorUserRepository;
import com.petralib.auth.security.model.SecurityUser;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConstructorUserDetailsService implements UserDetailsService {

    private final ConstructorUserRepository constructorUserRepository;

    @Value("${usr}")
    private String username;

    @Value("${psw}")
    private String password;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        ConstructorUserEntity user = constructorUserRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("User doesn't exist"));
//        if (!email.equals(username)){
//            throw new UsernameNotFoundException("User doesn't exist");
//        }
//        ConstructorUserEntity user = new ConstructorUserEntity();
//        constructorUserEntity.setId(1l);
//        constructorUserEntity.setName(username);
//        constructorUserEntity.setEmail(username);
//        constructorUserEntity.setPassword(password);
        return new SecurityUser(user);
    }
}
