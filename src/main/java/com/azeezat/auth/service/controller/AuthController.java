package com.azeezat.auth.service.controller;

import com.azeezat.auth.service.dto.*;
import com.azeezat.auth.service.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;


    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/register")
    public UserResponse registerUser(@Valid @RequestBody RegisterRequest registerRequest){
        return authService.registerUser(registerRequest);
    }


    @PostMapping("/login")
    public AuthResponse login (@Valid @RequestBody LoginRequest loginRequest){

      return authService.loginUser(loginRequest);
    }


    @GetMapping("/profile")
    public UserResponse getProfile(Authentication authentication) {
        return authService.getUserProfile(authentication.getName());
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(Authentication authentication) {
        return "Welcome Admin, " + authentication.getName();
    }

    @PatchMapping("/update-profile")
    public UserResponse updateProfile(Authentication authentication,
                                     @Valid @RequestBody UpdateProfileRequest request){
        return authService.updateProfile(authentication.getName(), request);
    }

    @PatchMapping("/change-password")
    public String changePassword(Authentication authentication,
                                @Valid @RequestBody ChangePasswordRequest request){
        authService.changePassword(authentication.getName(), request);
        return "Password changed successfully";
    }

    @PatchMapping("/change-email")
    public String changeEmail(Authentication authentication,
                                    @Valid @RequestBody ChangeEmailRequest changeEmailRequest){
        authService.changeEmail(authentication.getName(), changeEmailRequest);
        return "Email changed successfully";
    }



}