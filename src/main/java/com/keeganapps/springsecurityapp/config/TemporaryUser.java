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
    CommandLineRunner runner (ClientUserRepository clientUserRepository){
        return args -> {

            ClientUser defaultUser = new ClientUser(

                    "kelvinkeegen17@gmail.com",
                    "$2a$10$wwRRkaNiPN0c2P6lCxVTvO64zcyqC/1PA8o1FHktmhe79Bnzr4Rnm",
                    "Kelvin",
                    "Keegan",
                    ClientRoles.SUPER_USER,
                    LocalDate.now().toString()
            );

            clientUserRepository.save(defaultUser);

        };
    }

}
