package com.german.apirest.springboot.app.springbootcrud.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringSecurityConfig {
    
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * Construye el {@link SecurityFilterChain} que aplica:
     * <ul>
     *   <li>Permiso libre a las rutas <code>/users</code>.</li>
     *   <li>Autenticación obligatoria para cualquier otra petición.</li>
     *   <li>Desactivación de CSRF, ya que la API no usa formularios web.</li>
     *   <li>Política de sesión <strong>STATELESS</strong>, propia de APIs REST.</li>
     * </ul>
     *
     * @param http el objeto {@link HttpSecurity} para personalizar la seguridad HTTP
     * @return el {@link SecurityFilterChain} con la configuración aplicada
     * @throws Exception si ocurre algún error durante el proceso de configuración
     */
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/users").permitAll()
                .anyRequest().authenticated()
            )
            .csrf(config -> config.disable())
            .sessionManagement(managment -> managment
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .build();
    }

}
