package com.keeganapps.springsecurityapp.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Kelvin-Keegen on 25/Feb/2022 17:38
 */

@RequiredArgsConstructor
@Slf4j
public class TokenFactory {

    public Map<String,Object> GenerateTokens (User user, HttpServletRequest request) {

        String salt = "boy";

        Algorithm algorithm = Algorithm.HMAC256(salt.getBytes());

        // Access Token
        String accessToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(Date.valueOf(LocalDate.now().plusDays(7L)))
                .withIssuer(request.getServerName())
                .withClaim("roles",user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);

        // Refresh Token
        String refreshToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(Date.valueOf(LocalDate.now().plusDays(14L)))
                .withIssuer(request.getServerName())
                .sign(algorithm);

        // Sending Response

        Map<String,Object> mainResponse = new HashMap<>();
        mainResponse.put("statusCode",200);
        Map<String,String> responseBody = new HashMap<>();
        responseBody.put("accessToken",accessToken);
        responseBody.put("refreshToken",refreshToken);
        mainResponse.put("data",responseBody);
        mainResponse.put("message","Login Successful!");
        log.info("Tokens generated successfully");

        return mainResponse;

    }

}
