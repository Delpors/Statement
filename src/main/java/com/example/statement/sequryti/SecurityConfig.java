package com.example.statement.sequryti;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**")) // только для API отключаем

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/login",
                                "/login-error",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/webjars/**"
                        ).permitAll()

                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/users/**").hasRole("ADMIN")
                        .requestMatchers("/institutions/create/**").hasRole("ADMIN")

                        .requestMatchers("/employees/**").hasAnyRole("ADMIN", "OPERATOR")
                        .requestMatchers("/payroll/**").hasAnyRole("ADMIN", "OPERATOR")
                        .requestMatchers("/report/**").hasAnyRole("ADMIN", "OPERATOR")

                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/login")              // GET запрос для показа формы
                        .loginProcessingUrl("/login")     // POST запрос для обработки логина
                        .defaultSuccessUrl("/home", true) // после успешного входа
                        .failureUrl("/login?error=true")  // при ошибке
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )

                .sessionManagement(session -> session
                        .sessionFixation(sessionFixation -> sessionFixation.migrateSession())
                        .maximumSessions(1)               // одно устройство на пользователя
                        .expiredUrl("/login?expired=true")
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            request.getSession().setAttribute("userLoginTime", System.currentTimeMillis());
            response.sendRedirect("/home");
        };
    }
}