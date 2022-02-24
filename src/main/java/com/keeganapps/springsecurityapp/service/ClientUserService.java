package com.keeganapps.springsecurityapp.service;

import com.keeganapps.springsecurityapp.entity.tables.ClientUser;
import com.keeganapps.springsecurityapp.repository.ClientUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientUserService{

    private final ClientUserRepository clientUserRepository;


    public List<ClientUser> GetAllUsers() {

        return clientUserRepository.findAll();

    }

}
