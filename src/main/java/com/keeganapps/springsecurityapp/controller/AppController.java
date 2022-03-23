package com.keeganapps.springsecurityapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.keeganapps.springsecurityapp.config.security.AccessTokenRefresh;
import com.keeganapps.springsecurityapp.entity.models.PasswordChangeModel;
import com.keeganapps.springsecurityapp.entity.models.RegistrationModel;
import com.keeganapps.springsecurityapp.entity.models.StatusResponseBody;
import com.keeganapps.springsecurityapp.entity.tables.ClientUser;
import com.keeganapps.springsecurityapp.service.ClientUserManagementService;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
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

    private final ClientUserManagementService clientUserManagementService;
    private final AccessTokenRefresh accessTokenRefresh;


    @GetMapping(path = "/")
    public String AWShealthChecker() throws JsonProcessingException {

        ObjectMapper json = new ObjectMapper();
        return json.writeValueAsString(new StatusResponseBody(200,"Server health is good"));

    }

    @PostMapping("/api/v1/register")
    public StatusResponseBody Register_User(@RequestBody RegistrationModel registrationModel) {

        return clientUserManagementService.UserRegistration(registrationModel);
    }

    @GetMapping(path = "/api/v1/get-all")
    @SecurityRequirement(name = "API-Security")
    public StatusResponseBody Publish_All_ClientUsers() {

        return clientUserManagementService.GetAllUsers();
    }

    @PostMapping(path = "/api/v1/token-refresh")
    @SecurityRequirement(name = "API-Security")
    public String TokenRefresh(HttpServletRequest request, HttpServletResponse response) throws IOException {

        return accessTokenRefresh.RefreshAccessToken(request,response);
    }

    @PostMapping(path = "/api/v1/password-reset")
    public StatusResponseBody Password_Reset(@RequestParam String email) {

        return clientUserManagementService.PasswordReset(email);
    }

    @PostMapping(path = "/api/v1/password-change")
    @SecurityRequirement(name = "API-Security")
    public StatusResponseBody Password_Change(@RequestParam String email, @RequestBody PasswordChangeModel passwordChangeModel) {

        return clientUserManagementService.PasswordChange(email,passwordChangeModel);
    }

    @PostMapping(path = "/test-mail")
    @SecurityRequirement(name = "API-Security")
    public StatusResponseBody TestEmail(String email) {

        return clientUserManagementService.TestMail(email);
    }

}
