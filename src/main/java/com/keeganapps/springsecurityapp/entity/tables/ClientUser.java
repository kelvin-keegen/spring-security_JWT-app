package com.keeganapps.springsecurityapp.entity.tables;

import com.keeganapps.springsecurityapp.entity.models.ClientRoles;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "client_table",
        indexes = {

                @Index(name = "usernameIndex",columnList = "username"),
                @Index(name = "passwordIndex",columnList = "password"),
                @Index(name = "idIndex",columnList = "id")

        }
)
public class ClientUser implements UserDetails {

    @SequenceGenerator(

            name = "client_sequence",
            sequenceName = "client_sequence",
            allocationSize = 1
    )

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "client_sequence"
    )
    private Long id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    @Enumerated(EnumType.STRING)
    private ClientRoles clientRoles;
    private String createdAt;
    private Boolean isAccountEnabled = true;

    public ClientUser(String username, String password, String firstName, String lastName, ClientRoles clientRoles, String createdAt) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.clientRoles = clientRoles;
        this.createdAt = createdAt;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(clientRoles.name());
        return Collections.singletonList(grantedAuthority);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isAccountEnabled;
    }
}
