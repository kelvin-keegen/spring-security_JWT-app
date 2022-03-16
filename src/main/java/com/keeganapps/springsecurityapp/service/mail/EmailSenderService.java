package com.keeganapps.springsecurityapp.service.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailSenderService implements EmailSenderInterface {

    private final JavaMailSender javaMailSender;

    @Override
    public void Send(String to, String subject, String actualEmail) {

        try {

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,"utf-8");
            helper.setText(actualEmail,true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom("appsbykeegan@gmail.com");
            javaMailSender.send(message);

        } catch (MessagingException exception) {

            log.error("Unable to send Email");
            throw new IllegalStateException("Unable to send Email");
        }

    }
}
