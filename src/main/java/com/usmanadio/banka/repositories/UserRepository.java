package com.usmanadio.banka.repositories;

import com.usmanadio.banka.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
    User findById(UUID id);
    boolean existsByEmailOrPhoneNumber(String email, String phoneNumber);
    User findByEmailVerificationToken(String token);
}
