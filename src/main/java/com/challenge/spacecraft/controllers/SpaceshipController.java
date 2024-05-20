package com.challenge.spacecraft.controllers;

import com.challenge.spacecraft.dtos.spacecraft.CreateSpacecraftDto;
import com.challenge.spacecraft.dtos.spacecraft.UpdateSpacecraftDto;
import com.challenge.spacecraft.entities.Spaceship;
import com.challenge.spacecraft.responses.ErrorResponse;
import com.challenge.spacecraft.responses.SuccessResponse;
import com.challenge.spacecraft.services.SpaceshipService;
import jakarta.validation.Valid;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/spaceships")
@CacheConfig(cacheNames = "spaceships")
@RestController
public class SpaceshipController {

    private final SpaceshipService spaceshipService;

    public SpaceshipController(SpaceshipService spaceshipService) {
        this.spaceshipService = spaceshipService;
    }

    @GetMapping("")
    public ResponseEntity<List<Spaceship>> getSpaceships(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "20") int pageSize,
                                                        @RequestParam(required = false) String name) {
        Page<Spaceship> spaceships = spaceshipService.findAll(page, pageSize,name);
        return ResponseEntity.ok(spaceships.getContent());
    }

    @Cacheable(key = "#id")
    @GetMapping("/{id}")
    public ResponseEntity<?> getSpaceship(@PathVariable Integer id) {
        try{
            if(id < 1){
                throw new Exception("Spaceship id must be greater than 0");
            }
            Spaceship spaceship = this.spaceshipService.findById(id);
            return ResponseEntity.ok(spaceship);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }

    }

    @PostMapping("")
    public ResponseEntity<Spaceship> createSpaceship(@Valid @RequestBody CreateSpacecraftDto spaceship) {
        Spaceship spaceshipCreated = spaceshipService.save(spaceship);
        return ResponseEntity.ok(spaceshipCreated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSpaceship(@PathVariable Integer id) {
        try{
            if(id < 1){
                throw new Exception("Spaceship id must be greater than 0");
            }
            return ResponseEntity.ok().body(new SuccessResponse(spaceshipService.delete(id)));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> updateSpaceship(@PathVariable Integer id,@Valid @RequestBody UpdateSpacecraftDto spaceship) {
        try{
            if(id < 1){
                throw new Exception("Spaceship id must be greater than 0");
            }
            Spaceship spaceshipUpdated = this.spaceshipService.update(id, spaceship);
            return ResponseEntity.ok(spaceshipUpdated);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }
}
