package com.azeezat.auth.service.service;


import com.azeezat.auth.service.dto.RegisterRequest;
import com.azeezat.auth.service.dto.UserResponse;
import com.azeezat.auth.service.entity.Role;
import com.azeezat.auth.service.entity.User;
import com.azeezat.auth.service.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;


    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse registerUser(RegisterRequest registerRequest){
    if(userRepository.existsByEmail(registerRequest.getEmail())){

        throw new RuntimeException("Email already exist");

        }

        User user = new User();
            user.setFirstName(registerRequest.getFirstName());
            user.setLastName(registerRequest.getLastName());
            user.setEmail(registerRequest.getEmail());
           user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
           user.setRole(Role.CUSTOMER);
           User savedUser = userRepository.save(user);


    return new UserResponse(
            savedUser.getId(),
            savedUser.getFirstName(),
            savedUser.getLastName(),
            savedUser.getEmail());

    }

    }

