package com.keeganapps.springsecurityapp.service.mail;

public interface EmailSenderInterface {

    void Send(String to, String subject, String actualEmail);

}
