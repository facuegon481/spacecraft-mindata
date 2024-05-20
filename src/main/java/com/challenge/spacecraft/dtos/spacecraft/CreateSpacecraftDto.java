package com.challenge.spacecraft.dtos.spacecraft;

import jakarta.validation.constraints.NotBlank;

public class CreateSpacecraftDto {

    @NotBlank(message="You cannot create a spaceship without a name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
