package com.keeganapps.springsecurityapp.config;

import com.keeganapps.springsecurityapp.config.security.AppPasswordEncoder;
import com.keeganapps.springsecurityapp.entity.models.ClientRoles;
import com.keeganapps.springsecurityapp.entity.tables.ClientUser;
import com.keeganapps.springsecurityapp.repository.ClientUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;

@Configuration
public class TemporaryUser {


    @Bean
    CommandLineRunner runner (ClientUserRepository clientUserRepository, AppPasswordEncoder appPasswordEncoder){
        return args -> {

            ClientUser defaultUser = new ClientUser(

                    "kelvinkeegen17@gmail.com",
                    appPasswordEncoder.bCryptPasswordEncoder().encode("12345"),
                    "Kelvin",
                    "Keegan",
                    ClientRoles.SUPER_USER,
                    LocalDate.now().toString()
            );

            clientUserRepository.save(defaultUser);

        };
    }

}
