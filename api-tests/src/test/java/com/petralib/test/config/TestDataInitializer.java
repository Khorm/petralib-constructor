package com.petralib.test.config;

import com.petralib.auth.ConstructorUserRepository;
import com.petralib.auth.security.entity.ConstructorUserEntity;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@TestConfiguration
@Profile("test")
public class TestDataInitializer implements ApplicationRunner {

    private final ConstructorUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public TestDataInitializer(ConstructorUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        userRepository.findByEmail("r0meo1.ru@gmail.com")
                .orElseGet(() -> {
                    ConstructorUserEntity user = new ConstructorUserEntity();
                    user.setEmail("r0meo1.ru@gmail.com");
                    user.setName("Test User");
                    user.setPassword(passwordEncoder.encode("8K3uLnPVGTtcm5a"));
                    return userRepository.save(user);
                });
    }
}
