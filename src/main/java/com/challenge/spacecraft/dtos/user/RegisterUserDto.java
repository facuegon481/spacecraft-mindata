package com.challenge.spacecraft.dtos.user;
import jakarta.validation.constraints.NotBlank;


public class RegisterUserDto {
    @NotBlank(message="Email cannot be empty")
    private String email;

    @NotBlank(message="Password cannot be empty")
    private String password;

    @NotBlank(message="Full name cannot be empty")
    private String fullName;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
