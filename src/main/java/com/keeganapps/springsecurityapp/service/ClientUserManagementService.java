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
import com.keeganapps.springsecurityapp.service.mail.EmailValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientUserManagementService {

    private final ClientUserRepository clientUserRepository;
    private final AppPasswordEncoder appPasswordEncoder;
    private final EmailSenderService emailSenderService;
    private final PasswordManagement passwordManagement;


    // List all users from DB
    public StatusResponseBody GetAllUsers() {

        try {

            return new StatusResponseBody(200,clientUserRepository.findAll(),"Data retrieved");

        } catch (Exception exception) {

            log.error(exception.getMessage());
            return new StatusResponseBody(500,exception.getMessage());
        }

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

        //Catching all nasty exceptions :)
        try {

            emailSenderService.Send(registrationModel.getUsername(),"Account successfully created", new EmailBuilder().AccountCreated(registrationModel.getFirstName()));
            clientUserRepository.save(newUser);
            return new StatusResponseBody(200,"Account created successfully");

        } catch (Exception exception) {

            log.error(exception.getMessage());
            return new StatusResponseBody(500,exception.getMessage());

        }
    }

    // Reset password request service
    public StatusResponseBody PasswordReset(String email) {

        if (email.isEmpty()) {

            return new StatusResponseBody(400,"Bad request: Email is empty");
        }

        try {

            return passwordManagement.ResetPasswordFunction(email);

        } catch (Exception exception) {

            log.error(exception.getMessage());
            return new StatusResponseBody(500,exception.getMessage());
        }

    }

    public StatusResponseBody PasswordChange(String email, PasswordChangeModel passwordChangeModel) {

        if (email.isEmpty() || passwordChangeModel.getNewPassword().isEmpty()) {

            return new StatusResponseBody(400,"Bad request: Email is empty or password form is incomplete");
        }

        try {

            return passwordManagement.ChangePasswordFunction(email,passwordChangeModel);

        } catch (Exception exception) {

            log.error(exception.getMessage());
            return new StatusResponseBody(500,exception.getMessage());
        }
    }

    public StatusResponseBody TestMail(String email) {

        emailSenderService.Send(email,"TestMail",new EmailBuilder().AccountCreated("User"));
        return new StatusResponseBody(200,"Email sent successfully");
    }


    // End of Class
}
