package com.keeganapps.springsecurityapp.service.accountrecovery;

import com.keeganapps.springsecurityapp.config.security.AppPasswordEncoder;
import com.keeganapps.springsecurityapp.entity.models.PasswordChangeModel;
import com.keeganapps.springsecurityapp.entity.models.StatusResponseBody;
import com.keeganapps.springsecurityapp.entity.tables.ClientUser;
import com.keeganapps.springsecurityapp.repository.ClientUserRepository;
import com.keeganapps.springsecurityapp.service.mail.EmailBuilder;
import com.keeganapps.springsecurityapp.service.mail.EmailSenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordManagement {

    private final ClientUserRepository clientUserRepository;
    private final AppPasswordEncoder appPasswordEncoder;
    private final EmailSenderService emailSenderService;

    public StatusResponseBody ChangePasswordFunction(String email, PasswordChangeModel passwordChangeModel){

        if (email.isEmpty()){

           throw  new RuntimeException("Email is invalid or empty");
        }

        Optional<ClientUser> optionalClientUser = clientUserRepository.findByUsername(email);

        if (optionalClientUser.isEmpty()) {

            throw new RuntimeException("Email is not found");
        }

        String oldPassword = optionalClientUser.get().getPassword();

        if (appPasswordEncoder.bCryptPasswordEncoder().matches(passwordChangeModel.getOldPassword(),oldPassword)) {

            if (passwordChangeModel.getNewPassword().equals(passwordChangeModel.getConfirmPassword())){

                optionalClientUser.get()
                        .setPassword(appPasswordEncoder
                        .bCryptPasswordEncoder()
                        .encode(passwordChangeModel.getConfirmPassword()));


                emailSenderService.Send(email,"[Security Alert]: Password Changed!",new EmailBuilder().PasswordChanged(optionalClientUser.get().getFirstName()));
                clientUserRepository.save(optionalClientUser.get());

                return new StatusResponseBody(200,"Password has been changed");

            } else {

                return new StatusResponseBody(500,"New password does not match confirmed password");
            }
        }

        return new StatusResponseBody(400,"Old Password is incorrect please try again");
    }

    public StatusResponseBody ResetPasswordFunction(String email) {


            if (email.isEmpty()){

                throw  new RuntimeException("Email is invalid or empty");
            }

            Optional<ClientUser> optionalClientUser = clientUserRepository.findByUsername(email);

            if (optionalClientUser.isEmpty()) {

                throw new RuntimeException("Email is not found");
            }

            String newRandomPassword = RandomString.make(8);
            optionalClientUser.get().setPassword(appPasswordEncoder
                    .bCryptPasswordEncoder()
                    .encode(newRandomPassword));

            emailSenderService.Send(email,"[Security Alert]: Password Reset!",new EmailBuilder()
                    .PasswordResetMail(optionalClientUser.get().getFirstName(),newRandomPassword));
        clientUserRepository.save(optionalClientUser.get());

        return new StatusResponseBody(200,"Password reset successful");
    }


}
