package com.kamjritztex.solution.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.kamjritztex.solution.repository.UserRepo;




@Configuration
public class ApplicationConfig {

    @Autowired
    UserRepo userRepo;
    
    @Bean
    protected UserDetailsService userDetailsService(){
       return username -> userRepo.findById(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

   @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService());
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }
    @Bean 
    protected PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    


}