package com.keeganapps.springsecurityapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.keeganapps.springsecurityapp.config.security.AccessTokenRefresh;
import com.keeganapps.springsecurityapp.entity.models.StatusResponseBody;
import com.keeganapps.springsecurityapp.entity.tables.ClientUser;
import com.keeganapps.springsecurityapp.service.ClientUserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AppController {

    private final ClientUserService clientUserService;
    private final AccessTokenRefresh accessTokenRefresh;


    @GetMapping(path = "/")
    public String AWShealthChecker() throws JsonProcessingException {

        ObjectMapper json = new ObjectMapper();
        return json.writeValueAsString(new StatusResponseBody(200,"Server health is good"));

    }

    @GetMapping(path = "/api/v1/get-all")
    public List<ClientUser> Publish_All_ClientUsers() {

        return clientUserService.GetAllUsers();
    }

    @PostMapping(path = "api/v1/token-refresh")
    public String TokenRefresh(HttpServletRequest request) throws IOException {

        return accessTokenRefresh.RefreshAccessToken(request);
    }


}
