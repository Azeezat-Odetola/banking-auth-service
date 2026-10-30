package com.azeezat.auth.service.repository;

import com.azeezat.auth.service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {


     boolean existsByEmail(String email);


    Optional<User> findByEmail(String email);
}
