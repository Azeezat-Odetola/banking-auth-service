package com.azeezat.auth.service.repository;

import com.azeezat.auth.service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {


}
