package com.keeganapps.springsecurityapp.service;

import com.keeganapps.springsecurityapp.entity.tables.ClientUser;
import com.keeganapps.springsecurityapp.repository.ClientUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientUserService{

    private final ClientUserRepository clientUserRepository;


    public List<ClientUser> GetAllUsers() {

        return clientUserRepository.findAll();

    }

    public ClientUser getClientUser(String userName) {

       Optional<ClientUser> optionalClientUser = clientUserRepository.findByUsername(userName);

       if (optionalClientUser.isEmpty()) {

           throw new UsernameNotFoundException("User Not found with the username provided");
       }

        return optionalClientUser.get();
    }

}
