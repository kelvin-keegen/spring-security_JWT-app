package com.keeganapps.springsecurityapp.service.mail;

import org.springframework.stereotype.Service;

import java.util.function.Predicate;

@Service
public class EmailValidator implements Predicate<String> {
    @Override
    public boolean test(String email) {

        if(email != null || !email.isEmpty() ) {

            return true;
        }

        return false;
    }
}
