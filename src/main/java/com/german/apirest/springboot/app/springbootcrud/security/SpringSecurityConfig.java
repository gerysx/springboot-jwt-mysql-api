package com.german.apirest.springboot.app.springbootcrud.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de Spring Security para la aplicación.
 * Define cuántas y cuáles rutas están permitidas sin
 * autenticación, el manejo de CSRF y la política de sesión.
 */
@Configuration
public class SpringSecurityConfig {
    
    /**
     * Crea un {@link PasswordEncoder} basado en BCrypt.
     * Usado internamente por Spring Security para encriptar
     * y verificar contraseñas de usuarios.
     *
     * @return un encoder BCrypt para contraseñas
     */
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * Define la cadena de filtros de seguridad HTTP.
     * 
     * <ul>
     *   <li>Permite GET en /api/users sin autenticación.</li>
     *   <li>Permite POST en /api/users/register sin autenticación.</li>
     *   <li>Requiere autenticación para cualquier otra ruta.</li>
     *   <li>Deshabilita CSRF (ideal para APIs REST).</li>
     *   <li>Configura la aplicación como <strong>stateless</strong>, 
     *       es decir, sin mantener sesión en servidor.</li>
     * </ul>
     *
     * @param http el builder para configurar HTTP Security
     * @return la instancia de {@link SecurityFilterChain}
     * @throws Exception si ocurre un error de configuración
     */
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .authorizeHttpRequests(authz -> authz
                .requestMatchers(HttpMethod.GET,"/api/users").permitAll()
                .requestMatchers(HttpMethod.POST,"/api/users/register").permitAll()
                .anyRequest().authenticated()
            )
            .csrf(config -> config.disable())
            .sessionManagement(managment -> managment
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .build();
    }

}
