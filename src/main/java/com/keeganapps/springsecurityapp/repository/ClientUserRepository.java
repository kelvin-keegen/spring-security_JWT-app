package com.keeganapps.springsecurityapp.repository;

import com.keeganapps.springsecurityapp.entity.tables.ClientUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientUserRepository extends JpaRepository<ClientUser,Long> {

    Optional<ClientUser> findByUsername(String userName);

}
