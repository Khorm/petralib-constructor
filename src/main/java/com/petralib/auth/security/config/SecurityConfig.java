package com.petralib.auth.security.config;

import com.petralib.auth.security.jwt.JwtConfigure;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // проверка прав на уровне методов
@RequiredArgsConstructor
public class SecurityConfig implements WebMvcConfigurer {

    private final JwtConfigure jwtConfigure;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(configure -> configure.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                // Публичные эндпоинты
                .requestMatchers(HttpMethod.GET, "/").permitAll()
                .requestMatchers(HttpMethod.GET, "/error").permitAll()
                .requestMatchers("/api/v1/auth/login").permitAll()
                .requestMatchers("/api/v1/auth/register").permitAll() // если есть регистрация
                .requestMatchers("/login/**").permitAll()
                .requestMatchers("/*.css").permitAll()
                .requestMatchers("/*.ico").permitAll()
                .requestMatchers("/*.js").permitAll()

                // ===== ЗАЩИТА ПО РОЛЯМ (Role enum) =====
                // OWNER только для админ-панели
                .requestMatchers("/api/v1/admin/**").hasRole("OWNER")

                // MANAGER+ для создания/изменения проектов
                .requestMatchers(HttpMethod.POST, "/api/v1/projects/**").hasAnyRole("MANAGER", "OWNER")
                .requestMatchers(HttpMethod.PUT, "/api/v1/projects/**").hasAnyRole("MANAGER", "OWNER")
                .requestMatchers(HttpMethod.PATCH, "/api/v1/projects/**").hasAnyRole("MANAGER", "OWNER")

                // USER+ для чтения проектов (все вошедшие, кроме NONE)
                .requestMatchers(HttpMethod.GET, "/api/v1/projects/**").hasAnyRole("USER", "MANAGER", "OWNER")

                // ===== ТОНКАЯ ЗАЩИТА ПО ДЕЙСТВИЯМ (UserAction enum) =====
                // Полное удаление (только DELETE право)
                .requestMatchers("/api/v1/projects/*/permanent-delete").hasAuthority("DELETE")

                // Редактирование настроек (только EDIT право)
                .requestMatchers("/api/v1/projects/*/settings").hasAuthority("EDIT")

                // Экспорт данных (только READ право)
                .requestMatchers("/api/v1/projects/*/export").hasAuthority("READ")

                // Запись воркфлоу (только WRITE право)
                .requestMatchers("/api/v1/projects/*/workflows").hasAuthority("WRITE")

                // Все остальные API endpoints требуют аутентификации
                .requestMatchers("/api/v1/**").authenticated()
                // Все остальные запросы требуют аутентификации
                .anyRequest().authenticated()
        );
        jwtConfigure.configure(http);
        http.formLogin(form -> form
                .loginPage("/login")
                .permitAll()
        );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .build();
    }
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new SecurityRedirectInterceptor());
//    }


}
