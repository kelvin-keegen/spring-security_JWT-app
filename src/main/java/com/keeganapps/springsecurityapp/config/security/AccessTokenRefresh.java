package com.keeganapps.springsecurityapp.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.keeganapps.springsecurityapp.service.ClientDetailsService;
import com.keeganapps.springsecurityapp.utils.TokenFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;

/**
 * Created by Kelvin-Keegen on 25/Feb/2022 16:44
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class AccessTokenRefresh {

    private final ClientDetailsService clientDetailsService;

    public String RefreshAccessToken(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String salt = "boy";

        String requestHeader = request.getHeader(AUTHORIZATION);

        try {

            if (request.getHeader(AUTHORIZATION).startsWith("Bearer ") || requestHeader.isEmpty()) {

                try {

                    String refreshToken = requestHeader.substring("Bearer ".length());
                    Algorithm algorithm = Algorithm.HMAC256(salt.getBytes());
                    JWTVerifier verifier = JWT.require(algorithm).build();
                    DecodedJWT decodedJWT = verifier.verify(refreshToken);
                    String userName = decodedJWT.getSubject();
                    User user = (User) clientDetailsService.loadUserByUsername(userName);

                    return new ObjectMapper().writeValueAsString(new TokenFactory().GenerateTokens(user,request));

                } catch (Exception exception) {

                    // responding with an error from JWT
                    log.error("Error logging in: {}",exception.getMessage());
                    Map<String,Object> errorMessage = new HashMap<>();
                    errorMessage.put("statusCode",FORBIDDEN.value());
                    errorMessage.put("data",null);
                    errorMessage.put("message",exception.getMessage());
                    return new ObjectMapper().writeValueAsString(errorMessage);
                }

            } else {

                log.error("Something went wrong: Authorization header might have an invalid prefix, Please check the request");
                Map<String,Object> errorMessage = new HashMap<>();
                errorMessage.put("statusCode",FORBIDDEN.value());
                errorMessage.put("data",null);
                errorMessage.put("message","Something went wrong, Please check the request");
                return new ObjectMapper().writeValueAsString(errorMessage);

            }


        } catch (Exception exception) {

            log.error("[Exception caught], Please check the request. There might be no Authorization header");
            response.setStatus(FORBIDDEN.value());
            Map<String,Object> errorMessage = new HashMap<>();
            errorMessage.put("statusCode",FORBIDDEN.value());
            errorMessage.put("data",null);
            errorMessage.put("message","Something went wrong, Please check the request");
            return new ObjectMapper().writeValueAsString(errorMessage);
        }



    }

}
