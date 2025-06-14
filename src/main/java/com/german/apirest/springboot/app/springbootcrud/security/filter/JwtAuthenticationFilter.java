package com.german.apirest.springboot.app.springbootcrud.security.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.core.userdetails.User;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filtro de autenticación JWT que extiende de
 * UsernamePasswordAuthenticationFilter.
 * <p>
 * Lee credenciales en formato JSON, delega la autenticación al
 * AuthenticationManager
 * y genera un token JWT en caso de éxito.
 */
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    /**
     * Manager que procesa la autenticación de usuario.
     */
    private AuthenticationManager authenticationManager;

    /**
     * Clave secreta para firmar el token JWT.
     * Se construye usando un algoritmo HS256.
     */
    private static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();

    /**
     * Constructor que recibe el AuthenticationManager para delegar la
     * autenticación.
     *
     * @param authenticationManager instancia que valida las credenciales.
     */
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    /**
     * Intenta autenticar al usuario leyendo las credenciales del cuerpo de la
     * petición.
     *
     * @param request  petición HTTP con JSON de credenciales.
     * @param response respuesta HTTP.
     * @return Authentication autenticado si las credenciales son válidas.
     * @throws AuthenticationException si falla la autenticación.
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        User user = null;
        String username = null;
        String password = null;

        try {
            // Deserializa el JSON de login en la entidad User
            user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            username = user.getUsername();
            password = user.getPassword();
        } catch (StreamReadException e) {
            e.printStackTrace();
        } catch (DatabindException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Crea el token de autenticación con las credenciales proporcionadas
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
                password);
        // Delegar la autenticación al AuthenticationManager
        return authenticationManager.authenticate(authenticationToken);
    }

    /**
     * Maneja la autenticación exitosa generando un JWT y escribiéndolo en la
     * respuesta.
     *
     * @param request    petición HTTP original.
     * @param response   respuesta HTTP donde se incluirá el token.
     * @param chain      cadena de filtros.
     * @param authResult resultado de la autenticación.
     * @throws IOException      si falla la escritura en la respuesta.
     * @throws ServletException si ocurre un error en el filtro.
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult)
            throws IOException, ServletException {

        // Obtiene el UserDetails autenticado y extrae el username
        User user = (User) authResult.getPrincipal();
        String username = user.getUsername();

        // Genera el token JWT firmado
        String token = Jwts.builder()
                .subject(username)
                .signWith(SECRET_KEY)
                .compact();

        // Añade el token en la cabecera Authorization
        response.addHeader("Authorization", "Bearer " + token);

        // Construye el cuerpo de la respuesta en JSON
        Map<String, String> body = new HashMap<>();
        body.put("token", token);
        body.put("username", username);
        body.put("mesage", String.format("Hola %s has iniciado sesión con éxito", username));

        // Escribe el JSON en la respuesta
        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setContentType("application/json");
        response.setStatus(200);
    }
}
