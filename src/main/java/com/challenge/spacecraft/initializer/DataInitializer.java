package com.challenge.spacecraft.initializer;

import com.challenge.spacecraft.entities.Spaceship;
import com.challenge.spacecraft.entities.User;
import com.challenge.spacecraft.repositories.SpaceshipRepository;
import com.challenge.spacecraft.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SpaceshipRepository spaceshipRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        //Create a user
        User user = new User();
        user.setEmail("admin@admin.com");
        user.setPassword(passwordEncoder.encode("q1w2e3r4"));
        user.setFullName("Admin");
        this.userRepository.save(user);

        //Create spacecafts
        List<Spaceship> spaceships = new ArrayList<>();
        IntStream.rangeClosed(1, 50).forEach(i -> {
            Spaceship spaceship = new Spaceship();
            spaceship.setName("Spaceship " + i);
            spaceships.add(spaceship);
        });

        this.spaceshipRepository.saveAll(spaceships);
    }
}
