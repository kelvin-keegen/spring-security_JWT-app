package com.keeganapps.springsecurityapp.service;

import com.keeganapps.springsecurityapp.entity.tables.ClientUser;
import com.keeganapps.springsecurityapp.repository.ClientUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientDetailsService implements UserDetailsService {

    private final ClientUserRepository clientUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<ClientUser> optionalClientUser = clientUserRepository.findByUsername(username);

        if (optionalClientUser.isEmpty()){

         throw new UsernameNotFoundException("User Not found with the username provided");

        }

        return new User(
                optionalClientUser.get().getUsername(),
                optionalClientUser.get().getPassword(),
                optionalClientUser.get().getAuthorities()
        );
    }
}
