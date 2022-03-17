package com.keeganapps.springsecurityapp.entity.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
public class StatusResponseBody {

    private int statusCode;
    private Object data;
    private String message;

    public StatusResponseBody(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
