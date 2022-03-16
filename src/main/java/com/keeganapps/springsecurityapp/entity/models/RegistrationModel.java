package com.keeganapps.springsecurityapp.entity.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RegistrationModel {

    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private ClientRoles clientRoles;

}
