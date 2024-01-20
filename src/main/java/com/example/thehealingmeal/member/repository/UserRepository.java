package com.example.thehealingmeal.member.repository;

import com.example.thehealingmeal.member.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByLoginId(String loginId);

    boolean existsByPhoneNumber(String phoneNumber);

    Optional<User> findByLoginId(String loginId);

    boolean existsByName(String name);

    boolean existsByEmail(String email);

    Optional<User> findByEmailAndName(String email, String name);

    User findByEmail(String email);
}
