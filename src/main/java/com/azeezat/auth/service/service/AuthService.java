package com.azeezat.auth.service.service;

import com.azeezat.auth.service.dto.*;
import com.azeezat.auth.service.entity.Role;
import com.azeezat.auth.service.entity.User;
import com.azeezat.auth.service.exception.BadRequestException;
import com.azeezat.auth.service.exception.EmailAlreadyExistsException;
import com.azeezat.auth.service.exception.InvalidCredentialsException;
import com.azeezat.auth.service.exception.ResourceNotFoundException;
import com.azeezat.auth.service.repository.UserRepository;
import com.azeezat.auth.service.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public UserResponse registerUser(RegisterRequest registerRequest){
    if(userRepository.existsByEmail(registerRequest.getEmail())){

        throw new EmailAlreadyExistsException("Email already exist");

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

    public AuthResponse loginUser(@Valid LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));


        if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())){
            throw new InvalidCredentialsException("Invalid credentials");
        }
        String token = jwtService.generateToken(user.getEmail());
        return new AuthResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                token
        );
    }

    public UserResponse getUserProfile(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()
        );
    }

    public UserResponse updateProfile(String email, UpdateProfileRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }

        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }

        User updatedUser = userRepository.save(user);

        return new UserResponse(
                updatedUser.getId(),
                updatedUser.getFirstName(),
                updatedUser.getLastName(),
                updatedUser.getEmail()
        );
    }

    public void changePassword(String email, ChangePasswordRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new BadRequestException("Incorrect old password");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("New passwords do not match");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);


    }
    public void changeEmail(String email, ChangeEmailRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException("Incorrect password");
        }

        if (userRepository.existsByEmail(request.getNewEmail())) {
            throw new BadRequestException("Email already in use");
        }
        if (email.equalsIgnoreCase(request.getNewEmail())) {
            throw new BadRequestException("This is already your email address");
        }

        user.setEmail(request.getNewEmail());
        userRepository.save(user);
    }
}

