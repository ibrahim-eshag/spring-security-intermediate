package com.ibrahimeshag.basicspringsecutiywithjdbcuserdetailsmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class ProjectConfig {
    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        /*
        JdbcUSerDetailsManager expects the database to have the following columns
        User table:
        username, password, and enabled.
        authorities table:
         username, authority

        in that case returning the default instance of JdbcUserDetailsManager is enough, e.g:
        return new JdbcUserDetailsManager(dataSource);
        */

        // if the columns are different, then we need to add our custom implementation, as following
        String usersByUsernameQuery =
                "select username, password, enabled from users where username = ?";
        String authsByUserQuery =
                "select username, authority from authorities where username = ?";
        var userDetailsManager = new JdbcUserDetailsManager(dataSource);
        userDetailsManager.setUsersByUsernameQuery(usersByUsernameQuery);
        userDetailsManager.setAuthoritiesByUsernameQuery(authsByUserQuery);
        return userDetailsManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.httpBasic(Customizer.withDefaults());
        /* `http.httpBasic(Customizer.withDefaults())` configures Spring Security to parse this header and authenticate the user
          credentials against the configured authentication mechanism (in your case, `JdbcUserDetailsManager`).
         Without this configuration, Spring Security doesn't process the `Authorization` header for Basic Authentication,
         causing users to always be treated as unauthenticated (triggering the `AnonymousAuthenticationFilter`
        */

        http
                .authorizeHttpRequests(authorize -> authorize
                                .requestMatchers("/h2-console/**").permitAll()
//                        .requestMatchers("/hello").hasAuthority("write")
                                .anyRequest().authenticated()
                )
        ;

        http.headers(headers -> headers.disable());
        http.csrf(cs -> cs.disable());

        return http.build();
    }
}
