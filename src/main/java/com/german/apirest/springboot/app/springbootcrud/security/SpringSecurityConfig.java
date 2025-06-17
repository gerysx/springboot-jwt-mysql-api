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
 * Configuración de Spring Security para la aplicación.
 * <p>
 * Define:
 * <ul>
 *   <li>El {@link AuthenticationManager} basado en la configuración de Spring.</li>
 *   <li>El {@link PasswordEncoder} para encriptar y verificar contraseñas.</li>
 *   <li>La cadena de filtros HTTP que:
 *     <ul>
 *       <li>Permite rutas públicas (p. ej. registro y lectura de usuarios).</li>
 *       <li>Protege rutas según roles (ADMIN vs USER).</li>
 *       <li>Inyecta los filtros de autenticación y validación JWT.</li>
 *       <li>Deshabilita CSRF y activa el modo stateless.</li>
 *     </ul>
 *   </li>
 * </ul>
 * </p>
 */
@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig {

    /**
     * Configuración de autenticación de Spring, inyectada para
     * obtener el {@link AuthenticationManager}.
     */
    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    /**
     * Crea un {@link AuthenticationManager} a partir de la configuración
     * de Spring Security.
     * <p>
     * Este bean se usa internamente por los filtros de Spring para
     * delegar la autenticación de credenciales.
     * </p>
     *
     * @return la instancia de {@link AuthenticationManager}
     * @throws Exception si no puede obtenerse del contexto de Spring
     */
    @Bean
    AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();}


    /**
     * Crea un {@link PasswordEncoder} basado en BCrypt.
     * <p>
     * Se utiliza para:
     * <ul>
     *   <li>Encriptar la contraseña al registrar usuarios.</li>
     *   <li>Verificar la contraseña en el proceso de login.</li>
     * </ul>
     * </p>
     *
     * @return un encoder {@link BCryptPasswordEncoder}
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Define la cadena de filtros de seguridad HTTP.
     * <p>
     * Aplica las siguientes reglas:
     * <ul>
     *   <li>Permite GET en <code>/api/users</code> sin autenticación.</li>
     *   <li>Permite POST en <code>/api/users/register</code> sin autenticación.</li>
     *   <li>Requiere rol <code>ADMIN</code> para crear usuarios y productos.</li>
     *   <li>Permite a <code>USER</code> y <code>ADMIN</code> acceder a productos.</li>
     *   <li>Restringe DELETE de productos y usuarios a <code>ADMIN</code>.</li>
     *   <li>Requiere autenticación para cualquier otra ruta.</li>
     *   <li>Deshabilita CSRF y marca la sesión como <strong>stateless</strong>.</li>
     *   <li>Inyecta los filtros {@link JwtAuthenticationFilter} y
     *       {@link JwtValidationFilter} para gestionar JWT.</li>
     * </ul>
     * </p>
     *
     * @param http el builder para configurar la seguridad HTTP
     * @return la instancia de {@link SecurityFilterChain} configurada
     * @throws Exception si ocurre un error durante la configuración
     */
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(HttpMethod.GET, "/api/users").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/users/register").permitAll()
                        /* .requestMatchers(HttpMethod.POST, "/api/users").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/products").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/products", "/api/products/{id}")
                        .hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.DELETE, "/api/products/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/users/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/{id}").hasRole("ADMIN") */
                        .anyRequest().authenticated())
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))
                .addFilter(new JwtValidationFilter(authenticationManager()))
                .csrf(config -> config.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(managment ->
                    managment.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource (){
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(Arrays.asList("*"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PUT"));
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;

    }

    @Bean
    FilterRegistrationBean<CorsFilter> corsFilter () {
        FilterRegistrationBean<CorsFilter> corsBean = new FilterRegistrationBean<>(new CorsFilter(corsConfigurationSource()));
        corsBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return corsBean;
    }
}
