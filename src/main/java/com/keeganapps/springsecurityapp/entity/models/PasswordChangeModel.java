package com.keeganapps.springsecurityapp.entity.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PasswordChangeModel {

    private String oldPassword;
    private String newPassword;
    private String confirmPassword;

}
