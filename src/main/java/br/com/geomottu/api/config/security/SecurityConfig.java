package br.com.geomottu.api.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(a -> a
                        // Permite acesso público à página de login e recursos estáticos
                        .requestMatchers("/login", "/css/**", "/js/**", "/images/**").permitAll()
                        // Apenas ADMIN pode acessar as páginas de filiais
                        .requestMatchers("/filiais/**", "/admin/**").hasRole("ADMIN")
                ).build();
    }
}
