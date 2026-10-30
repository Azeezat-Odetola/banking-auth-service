package com.azeezat.auth.service.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class ChangeEmailRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String newEmail;

    @NotBlank(message = "Current password is required")
    private String currentPassword;

    public ChangeEmailRequest() {
    }

    public String getNewEmail() {
        return newEmail;
    }

    public void setNewEmail(String newEmail) {
        this.newEmail = newEmail;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

}
