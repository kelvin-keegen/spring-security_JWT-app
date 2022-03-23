package com.keeganapps.springsecurityapp.service.mail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class EmailValidator implements Predicate<String> {

    @Override
    public boolean test(String email) {


        if(!email.isEmpty() || email != null) {

            boolean found = false;

            Pattern pattern = Pattern.compile("@",Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(email);
            found = matcher.find();

            if (found) {

                return true;
            }

            log.error("Email could not be sent!, Invalid user email address: {}",email);
            return false;
        }

        return false;
    }
}
