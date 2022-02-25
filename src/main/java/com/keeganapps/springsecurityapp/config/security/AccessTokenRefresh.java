package com.keeganapps.springsecurityapp.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.keeganapps.springsecurityapp.service.ClientDetailsService;
import com.keeganapps.springsecurityapp.service.ClientUserService;
import com.keeganapps.springsecurityapp.utils.TokenFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
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

    public String RefreshAccessToken(HttpServletRequest request) throws IOException {

        String salt = "boy";

        String requestHeader = request.getHeader(AUTHORIZATION);

        if (requestHeader.startsWith("Bearer ")) {

            try {

                String refreshToken = requestHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256(salt.getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refreshToken);
                String userName = decodedJWT.getSubject();
                User user = (User) clientDetailsService.loadUserByUsername(userName);

               return new ObjectMapper().writeValueAsString(new TokenFactory().GenerateTokens(user,request));

            } catch (Exception exception) {

                // responding with an error
                log.error("Error logging in: {}",exception.getMessage());
                Map<String,Object> errorMessage = new HashMap<>();
                errorMessage.put("status",FORBIDDEN.value());
                errorMessage.put("message",exception.getMessage());
               return new ObjectMapper().writeValueAsString(errorMessage);
            }

        } else {

            log.error("Something went wrong, Please check the request");
            throw new RuntimeException("Something went wrong, Please check the request");
        }

    }

}
