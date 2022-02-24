package com.keeganapps.springsecurityapp.entity.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
public class StatusResponseBody {

    private int status;
    private String message;

}
