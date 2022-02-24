package com.keeganapps.springsecurityapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.keeganapps.springsecurityapp.entity.models.StatusResponseBody;
import com.keeganapps.springsecurityapp.entity.tables.ClientUser;
import com.keeganapps.springsecurityapp.service.ClientUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AppController {

    private final ClientUserService clientUserService;


    @GetMapping(path = "/")
    public String AWShealthChecker() {

        ObjectMapper json = new ObjectMapper();
        try {
            return json.writeValueAsString(new StatusResponseBody(200,"Server health is good"));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping(path = "/api/v1/get-all")
    public List<ClientUser> Publish_All_ClientUsers() {

        return clientUserService.GetAllUsers();
    }


}
