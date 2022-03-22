package com.keeganapps.springsecurityapp.service;

import com.keeganapps.springsecurityapp.config.security.AppPasswordEncoder;
import com.keeganapps.springsecurityapp.entity.models.PasswordChangeModel;
import com.keeganapps.springsecurityapp.entity.models.RegistrationModel;
import com.keeganapps.springsecurityapp.entity.models.StatusResponseBody;
import com.keeganapps.springsecurityapp.entity.tables.ClientUser;
import com.keeganapps.springsecurityapp.repository.ClientUserRepository;
import com.keeganapps.springsecurityapp.service.accountrecovery.PasswordManagement;
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
    private final PasswordManagement passwordManagement;


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
    public StatusResponseBody UserRegistration(RegistrationModel registrationModel) {

        if (registrationModel.getUsername().isEmpty()) {

            return new StatusResponseBody(400,"Bad Request: Email address is empty");
        }

        Optional<ClientUser> optionalClientUser = clientUserRepository.findByUsername(registrationModel.getUsername());

        if (optionalClientUser.isPresent()){

            return new StatusResponseBody(500,"User with this email already exists");
        }

        String encodedPassword = appPasswordEncoder.bCryptPasswordEncoder().encode(registrationModel.getPassword());
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = dateTimeFormatter.format(LocalDateTime.now(ZoneId.of("GMT")));

        ClientUser newUser = new ClientUser(

                registrationModel.getUsername(),
                encodedPassword,
                registrationModel.getFirstName(),
                registrationModel.getLastName(),
                registrationModel.getClientRoles(),
                formattedTime
        );

        clientUserRepository.save(newUser);
        emailSenderService.Send(registrationModel.getUsername(),"Account successfully created", new EmailBuilder().AccountCreated(registrationModel.getFirstName()));
        return new StatusResponseBody(200,"Account created successfully");
    }

    // Reset password request service
    public StatusResponseBody PasswordReset(String email) {

        if (email.isEmpty()) {

            return new StatusResponseBody(400,"Bad request: Email is empty");
        }

        return passwordManagement.ResetPasswordFunction(email);

    }

    public StatusResponseBody PasswordChange(String email, PasswordChangeModel passwordChangeModel) {

        if (email.isEmpty() || passwordChangeModel.getNewPassword().isEmpty()) {

            return new StatusResponseBody(400,"Bad request: Email is empty or password form is incomplete");
        }

        return passwordManagement.ChangePasswordFunction(email,passwordChangeModel);
    }
}
