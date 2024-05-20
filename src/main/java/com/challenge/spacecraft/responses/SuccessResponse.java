package com.challenge.spacecraft.responses;

public class SuccessResponse {
    private String message;

    public SuccessResponse(String message) {
        this.message = message;
    }

    // Getter y Setter
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
