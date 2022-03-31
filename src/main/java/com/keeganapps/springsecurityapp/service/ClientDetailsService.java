package com.keeganapps.springsecurityapp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.keeganapps.springsecurityapp.entity.models.StatusResponseBody;
import com.keeganapps.springsecurityapp.entity.tables.ClientUser;
import com.keeganapps.springsecurityapp.repository.ClientUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientDetailsService implements UserDetailsService {

    private final ClientUserRepository clientUserRepository;
    private final HttpServletResponse httpServletResponse;

    // Spring security util class for user validation

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<ClientUser> optionalClientUser = clientUserRepository.findByUsername(username);

        if (optionalClientUser.isEmpty()){

            ObjectMapper objectMapper = new ObjectMapper();

         httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
         httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
            try {
                objectMapper.writeValue(httpServletResponse.getOutputStream(),new StatusResponseBody(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,null,"User Not found with the username provided"));
                log.error("User Not found with the username provided");

            } catch (IOException e) {

                log.error(e.getMessage());
            }
        }

            return new User(
                    optionalClientUser.get().getUsername(),
                    optionalClientUser.get().getPassword(),
                    optionalClientUser.get().getAuthorities()
            );

    }
}
