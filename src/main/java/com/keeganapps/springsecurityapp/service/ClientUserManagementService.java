package com.keeganapps.springsecurityapp.service;

import com.keeganapps.springsecurityapp.config.security.AppPasswordEncoder;
import com.keeganapps.springsecurityapp.entity.models.RegistrationModel;
import com.keeganapps.springsecurityapp.entity.models.StatusResponseBody;
import com.keeganapps.springsecurityapp.entity.tables.ClientUser;
import com.keeganapps.springsecurityapp.repository.ClientUserRepository;
import com.keeganapps.springsecurityapp.service.mail.EmailBuilder;
import com.keeganapps.springsecurityapp.service.mail.EmailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientUserManagementService {

    private final ClientUserRepository clientUserRepository;
    private final AppPasswordEncoder appPasswordEncoder;
    private final EmailSenderService emailSenderService;


    // List all users from DB
    public List<ClientUser> GetAllUsers() {

        return clientUserRepository.findAll();

    }

    // Get a specific client for DB

    public ClientUser getClientUser(String userName) {

       Optional<ClientUser> optionalClientUser = clientUserRepository.findByUsername(userName);

       if (optionalClientUser.isEmpty()) {

           throw new UsernameNotFoundException("User Not found with the username provided");
       }

        return optionalClientUser.get();
    }

    // Register new users
    public StatusResponseBody UserRegistration(RegistrationModel newClientUser) {

        if (newClientUser.getUsername().isEmpty()) {

            throw new RuntimeException("email address is empty");
        }

        Optional<ClientUser> optionalClientUser = clientUserRepository.findByUsername(newClientUser.getUsername());

        if (optionalClientUser.isPresent()){

            return new StatusResponseBody(500,"User with this email already exists");
        }

        String encodedPassword = appPasswordEncoder.bCryptPasswordEncoder().encode(newClientUser.getPassword());
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = dateTimeFormatter.format(LocalDateTime.now(ZoneId.of("GMT")));

        ClientUser newUser = new ClientUser(

                newClientUser.getUsername(),
                encodedPassword,
                newClientUser.getFirstName(),
                newClientUser.getLastName(),
                newClientUser.getClientRoles(),
                formattedTime
        );

        clientUserRepository.save(newUser);
        emailSenderService.Send(newClientUser.getUsername(),"Account successfully created", new EmailBuilder().AccountCreated(newClientUser.getFirstName()));
        return new StatusResponseBody(200,"Account created successfully");
    }

}
