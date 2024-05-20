# Spaceship CRUD Challenge

## Requirements

- Maven Apache
- Java 21
- Docker

## About

This repository presents a challenge aimed at creating a CRUD (Create, Read, Update, Delete) application for spaceships. The goal is to provide a simple yet functional system for managing spaceships.

## Usage

1. Clone the repository.
2. Enter the cloned project directory:
   ```bash
   cd <your-cloned-project-directory>
3. Build and .jar:
   ```bash
   mvn package
4. Build the Docker image and wait until the process finishes:
   ```bash
    docker build -t spacecraftapi .
5. Run the Docker container and wait until the process finishes:
   ```bash
    docker run -p 8000:8080 spacecraftapi
6. Now the application is running on port 8000. (You can change the ports in step 5, but you will need to update the Swagger UI and API calls accordingly.)
   
## Security

The API implements a JWT (JSON Web Token) based security system. To access protected endpoints, you'll need to provide a valid JWT token. For testing purposes, you can use the following credentials:

- Email: admin@admin.com
- Password: q1w2e3r4

## Endpoints

For more detailed documentation on each endpoint and how to use them, refer to the Swagger UI documentation available at http://localhost:8000/swagger-ui/index.html.

## Integration Tests

The repository includes integration tests for both controllers within the REST API. These tests ensure that the endpoints behave as expected and provide coverage for key functionalities of the application.
