package com.challenge.spacecraft.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
@Component
public class SpaceshipAspect {

    private static final Logger logger = LoggerFactory.getLogger(SpaceshipAspect.class);

    @Before("execution(* com.challenge.spacecraft.controllers.SpaceshipController.getSpaceship(..)) && args(id,..)")
    public void logIfNegativeId(JoinPoint joinPoint, Integer id) {
        if (id < 1) {
            logger.warn("Attempted to get spaceship with invalid id: {}", id);
        }
    }
}