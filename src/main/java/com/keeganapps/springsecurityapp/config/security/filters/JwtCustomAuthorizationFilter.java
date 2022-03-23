package com.keeganapps.springsecurityapp.config.security.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;

/**
 * Created by Kelvin-Keegen on 25/Feb/2022 14:34
 */

@Slf4j
public class JwtCustomAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // Check if request is non-operational
        if (request.getServletPath().equals("/") || request.getServletPath().equals("/api/v1/login") ||
                request.getServletPath().equals("/api/v1/token-refresh") ||
                request.getServletPath().equals("/api/v1/register") ||
                request.getServletPath().equals("/api/v1/password-reset")) {

            filterChain.doFilter(request,response);
        } else {

            // Checking to see if token is present in request
            String authHeader = request.getHeader(AUTHORIZATION);

            if (authHeader != null && authHeader.startsWith("Bearer ")) {

                try {

                    String token = authHeader.substring("Bearer ".length());
                    Algorithm algorithm = Algorithm.HMAC256("boy".getBytes());
                    JWTVerifier verifier = JWT.require(algorithm).build();
                    DecodedJWT decodedJWT = verifier.verify(token);
                    String userName = decodedJWT.getSubject();
                    String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    stream(roles).forEach(role ->{
                        authorities.add(new SimpleGrantedAuthority(role));
                    });
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userName,null,authorities);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request,response);

                }catch (Exception exception) {

                    // responding with an error
                    log.error("Error logging in: {}",exception.getMessage());
                    response.setStatus(FORBIDDEN.value());
                    Map<String,Object> errorMessage = new HashMap<>();
                    errorMessage.put("statusCode",FORBIDDEN.value());
                    errorMessage.put("data",null);
                    errorMessage.put("message",exception.getMessage());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(),errorMessage);
                }

            } else {

                log.error("Please check your request, Token not included on request to server. servlet path:{}",request.getServletPath());
                response.setStatus(FORBIDDEN.value());
                Map<String,Object> badRequestMessage = new HashMap<>();
                badRequestMessage.put("statusCode",FORBIDDEN.value());
                badRequestMessage.put("data",null);
                badRequestMessage.put("message","Requested resource is forbidden!");
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),badRequestMessage);

            }
        }
    }
}
