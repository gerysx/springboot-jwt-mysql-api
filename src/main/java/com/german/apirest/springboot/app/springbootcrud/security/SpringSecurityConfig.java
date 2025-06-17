package com.german.apirest.springboot.app.springbootcrud.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.german.apirest.springboot.app.springbootcrud.security.filter.JwtAuthenticationFilter;
import com.german.apirest.springboot.app.springbootcrud.security.filter.JwtValidationFilter;

/**
 * Configuración de seguridad basada en filtros para la aplicación.
 * <p>
 * Define beans y la cadena de filtros que:
 * <ul>
 *   <li>Proporciona un {@link org.springframework.security.authentication.AuthenticationManager}</li>
 *   <li>Declara un {@link org.springframework.security.crypto.password.PasswordEncoder} (BCrypt)</li>
 *   <li>Configura CORS para permitir orígenes y métodos específicos</li>
 *   <li>Deshabilita CSRF y establece la política de sesión como <strong>stateless</strong></li>
 *   <li>Inyecta los filtros {@link com.german.apirest.springboot.app.springbootcrud.security.filter.JwtAuthenticationFilter}
 *       y {@link com.german.apirest.springboot.app.springbootcrud.security.filter.JwtValidationFilter}
 *       para gestionar la autenticación y validación de JWT.</li>
 *   <li>Define reglas de autorización por endpoint (públicos vs. protegidos).</li>
 * </ul>
 * </p>
 *
 * @version 1.0
 * @since   1.0
 */
@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig {

    /**
     * Configuración de autenticación inyectada para obtener el {@link AuthenticationManager}.
     */
    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    /**
     * Crea un {@link AuthenticationManager} a partir de la configuración de Spring Security.
     *
     * @return instancia de {@code AuthenticationManager}
     * @throws Exception si no puede obtenerse del contexto de Spring
     */
    @Bean
    AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Crea un {@link PasswordEncoder} basado en {@link BCryptPasswordEncoder}.
     * <p>
     * Utilizado para encriptar contraseñas al registrar usuarios y
     * verificarlas durante el login.</p>
     *
     * @return instancia de {@code BCryptPasswordEncoder}
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configura la cadena de filtros HTTP para seguridad.
     * <p>
     * Define reglas de acceso por ruta, deshabilita CSRF,
     * habilita CORS, establece sesiones <strong>stateless</strong>
     * e intercala filtros de JWT.</p>
     *
     * @param http builder de {@link HttpSecurity}
     * @return la {@code SecurityFilterChain} configurada
     * @throws Exception si ocurre un error durante la configuración
     */
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .authorizeHttpRequests(authz -> authz
                .requestMatchers(HttpMethod.GET, "/api/users").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/users/register").permitAll()
                .anyRequest().authenticated()
            )
            .addFilter(new JwtAuthenticationFilter(authenticationManager()))
            .addFilter(new JwtValidationFilter(authenticationManager()))
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(sess ->
                sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .build();
    }

    /**
     * Define la configuración CORS global para la aplicación.
     *
     * @return {@code CorsConfigurationSource} con orígenes, métodos y cabeceras permitidos
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(Arrays.asList("*"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PUT"));
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    /**
     * Registra el filtro CORS con la precedencia más alta.
     *
     * @return {@code FilterRegistrationBean<CorsFilter>} para el filtro de CORS
     */
    @Bean
    FilterRegistrationBean<CorsFilter> corsFilter() {
        FilterRegistrationBean<CorsFilter> bean =
            new FilterRegistrationBean<>(new CorsFilter(corsConfigurationSource()));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }
}