package com.keeganapps.springsecurityapp.config.security;

import com.keeganapps.springsecurityapp.config.security.filters.JwtCustomAuthorizationFilter;
import com.keeganapps.springsecurityapp.config.security.filters.JwtCustomUserNamePasswordFilter;
import com.keeganapps.springsecurityapp.entity.models.ClientRoles;
import com.keeganapps.springsecurityapp.service.ClientDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurityConfigs extends WebSecurityConfigurerAdapter {

    private final AppPasswordEncoder appPasswordEncoder;
    private final ClientDetailsService clientDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // Creating custom auth filter instance
        JwtCustomUserNamePasswordFilter jwtCustomUserNamePasswordFilter = new JwtCustomUserNamePasswordFilter(authenticationManagerBean());
        jwtCustomUserNamePasswordFilter.setFilterProcessesUrl("/api/v1/login");

        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers("/").permitAll();
        http.authorizeRequests().antMatchers("/api/v1/register").permitAll();
        http.authorizeRequests().antMatchers("/api/v1/login").permitAll();
        http.authorizeRequests().antMatchers("/api/v1/token-refresh").permitAll();
        http.authorizeRequests().antMatchers("/api/v1/password-reset").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.POST,"/api/v1/delete/**").hasAnyAuthority(String.valueOf(ClientRoles.ADMIN));
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(jwtCustomUserNamePasswordFilter);
        http.addFilterBefore(new JwtCustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);


    }

    // Added ignored URLs for swagger documentation
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers(
                        "/v2/api-docs/",
                        "/v3/api-docs/**",
                        "/swagger-resources/**",
                        "/swagger-ui.html/**",
                        "/swagger-ui/index.html",
                        "/favicon.ico",
                        "/swagger-ui/**",
                        "/webjars/**");
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {

        return super.authenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(clientDetailsService);
        provider.setPasswordEncoder(appPasswordEncoder.bCryptPasswordEncoder());
        return provider;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(false);
        configuration.addAllowedOrigin(CorsConfiguration.ALL);
        configuration.addAllowedHeader(CorsConfiguration.ALL);
        configuration.addAllowedMethod(CorsConfiguration.ALL);
        configuration.setMaxAge(600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}
