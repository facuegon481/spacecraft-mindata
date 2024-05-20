package com.challenge.spacecraft.repositories;

import com.challenge.spacecraft.entities.Spaceship;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface SpaceshipRepository extends JpaRepository<Spaceship, Integer> {
    Page<Spaceship> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
