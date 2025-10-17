package com.gestion.eventos.Config;

import com.gestion.eventos.Repository.IUsuarioRepository;
import com.gestion.eventos.Security.JwtAuthenticationFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers(
                    "/usuarios/login",
                    "/usuarios/registrar",
                    "/usuarios/recuperar",
                    "/programas/registrar",
                    "/facultad/registrar",
                    "/unidad/registrar",
                    "/espacio/registrar",
                    "/error",
                    "/",            
                    "/static/**",                    
                    "/images/**"
                ).permitAll()
                .requestMatchers("/organizacionExterna/**", "/eventos/**")
                .hasAnyRole("ESTUDIANTE", "DOCENTE")

                .requestMatchers("/evaluacion/**")
                    .hasRole("SECRETARIA_ACADEMICA")

                .anyRequest().authenticated()
            );

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(IUsuarioRepository usuarioRepository) {
        return username -> usuarioRepository.findByCorreoInstitucional(username)
            .map(u -> {
                UserDetails user = User
                    .withUsername(u.getCorreoInstitucional())
                    .password(u.getContrasena())
                    .roles(u.getRol().name().toUpperCase())
                    .build();
                return user;
            })
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}