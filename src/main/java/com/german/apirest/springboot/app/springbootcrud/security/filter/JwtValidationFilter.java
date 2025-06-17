package com.german.apirest.springboot.app.springbootcrud.security.filter;

import static com.german.apirest.springboot.app.springbootcrud.security.TokenJwtConfig.CONTENT_TYPE;
import static com.german.apirest.springboot.app.springbootcrud.security.TokenJwtConfig.HEADER_AUTHORIZATION;
import static com.german.apirest.springboot.app.springbootcrud.security.TokenJwtConfig.PREFIX_TOKEN;
import static com.german.apirest.springboot.app.springbootcrud.security.TokenJwtConfig.SECRET_KEY;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.german.apirest.springboot.app.springbootcrud.security.SimpleGrantedAuthorityJsonCreator;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filtro de validación JWT que extiende de
 * {@link BasicAuthenticationFilter}.
 * <p>
 * Intercepta cada petición, extrae y valida el token JWT,
 * establece la autenticación en el contexto si es válido
 * o devuelve 401 en caso contrario.
 * </p>
 */
public class JwtValidationFilter extends BasicAuthenticationFilter{

/**
     * Constructor que recibe el {@code AuthenticationManager} para validar el token.
     *
     * @param authenticationManager gestor de autenticación de Spring.
     */
    public JwtValidationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }


/**
     * Filtra cada petición HTTP verificando el JWT.
     *
     * @param request  petición entrante.
     * @param response respuesta HTTP.
     * @param chain    cadena de filtros.
     * @throws IOException      si ocurre un error de I/O.
     * @throws ServletException si ocurre un error en el servlet.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

                String header = request.getHeader(HEADER_AUTHORIZATION);

                if(header== null ||!header.startsWith(PREFIX_TOKEN)) {
                    chain.doFilter(request, response);
                    return;
                }
                String token = header.replace(PREFIX_TOKEN, "");

                try {
                    Claims claims = Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token).getPayload();
                    String username = claims.getSubject();
                    Object authoritiesClaims = claims.get("authorities");

                    Collection<? extends GrantedAuthority> authorities = Arrays.asList(
                        new ObjectMapper()
                        .addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityJsonCreator.class)
                        .readValue(authoritiesClaims.toString().getBytes(),SimpleGrantedAuthority[].class));

                    UsernamePasswordAuthenticationToken authenticationToken= new UsernamePasswordAuthenticationToken(username, null,  authorities);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    chain.doFilter(request, response);
                } catch (JwtException e) {
                    Map<String, String> body = new HashMap<>();
                    body.put("error", e.getMessage());
                    body.put("message", "El token JWT no es válido");

                    response.getWriter().write(new ObjectMapper().writeValueAsString(body));
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.setContentType(CONTENT_TYPE);
                }

    }
    
    
}
