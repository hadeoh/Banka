package com.usmanadio.banka.security;

import com.usmanadio.banka.exceptions.CustomException;
import com.usmanadio.banka.models.user.User;
import com.usmanadio.banka.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new CustomException("Email not found", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return org.springframework.security.core.userdetails.User.withUsername(email)
                .password(user.getPassword()).authorities(user.getRoles())
                .accountExpired(false).accountLocked(false)
                .credentialsExpired(false).disabled(false).build();
    }
}
