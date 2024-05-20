package com.challenge.spacecraft.services;

import com.challenge.spacecraft.dtos.spacecraft.CreateSpacecraftDto;
import com.challenge.spacecraft.dtos.spacecraft.UpdateSpacecraftDto;
import com.challenge.spacecraft.entities.Spaceship;
import com.challenge.spacecraft.repositories.SpaceshipRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SpaceshipService {
    private final SpaceshipRepository spaceshipRepository;

    public SpaceshipService(SpaceshipRepository spaceshipRepository) {
        this.spaceshipRepository = spaceshipRepository;
    }

    public Page<Spaceship> findAll(int pageNumber, int pageSize, String name) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        if (name == null || name.isEmpty()) {
            return spaceshipRepository.findAll(pageable);
        } else {
            return spaceshipRepository.findByNameContainingIgnoreCase(name, pageable);
        }
    }

    public Spaceship findById(Integer id) {
        return this.spaceshipRepository.findById(id).orElse(null);
    }

    public Spaceship save(CreateSpacecraftDto spaceshipDto) {
        Spaceship spaceship = new Spaceship();
        spaceship.setName(spaceshipDto.getName());

        return spaceshipRepository.save(spaceship);
    }

    public String delete(Integer id) throws Exception {
        Optional<Spaceship> spaceshipOptional = spaceshipRepository.findById(id);
        if (!spaceshipOptional.isPresent()) {
            throw new Exception("ID not exists");
        }
        spaceshipRepository.deleteById(id);

        return "Spaceship deleted";
    }

    public Spaceship update(Integer id, UpdateSpacecraftDto spaceshipDto) throws Exception {
        Spaceship spaceship = this.spaceshipRepository.findById(id).orElse(null);
        if(spaceship == null) {
            throw new Exception("Id not found");
        }

        spaceship.setName(spaceshipDto.getName());
        return spaceshipRepository.save(spaceship);
    }
}
