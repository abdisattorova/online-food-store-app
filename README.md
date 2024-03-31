## Online Food Store Management Server

This project is an online food store management server built using Java 17 and Spring Boot 3.0.5. It utilizes Redis for caching and Docker for containerization.

### Table of Contents

1. [Introduction](#introduction)
2. [Technologies Used](#technologies-used)
3. [Important Links](#important-links)
4. [Getting Started](#getting-started)
5. [Project Structure](#project-structure)
6. [Running the Project](#running-the-project)
7. [Testing Users](#testing-users)
8. [Swagger Documentation](#swagger-documentation)

### Introduction

This project aims to provide a backend server for managing an online food store. It includes functionalities for managing food items.

### Technologies Used

- Java 17
- Spring Boot 3.0.5
- Redis
- Docker

### Important Links

- [Technical Task Document](https://drive.google.com/file/d/1VHo6OVhZjrXFgZrNTvE_Gdv_avZYk1RV/view?usp=sharing)
- [Entity Relationship Diagram (ERD)](https://drive.google.com/file/d/1tWZkaYrjsAT3n_Hw7p1-BiMMEXoU2rqU/view?usp=sharing)

### Getting Started

To get started with the project, follow these steps:

1. Clone the repository.
2. Review the technical task document for an overview of the project requirements.
3. Check the ERD to understand the database schema and relationships.
4. Install Docker if not already installed.
5. Build and run the project using Spring Boot.

### Project Structure

- **src/main/java**: Contains Java source code.
- **src/main/resources**: Contains application properties and static resources.

### Running the Project

#### Using Docker Compose

1. Build the Docker images:
    ```bash
    docker-compose build
    ```

2. Start the containers:
    ```bash
    docker-compose up
    ```

#### Using Docker Hub Image

1. Pull the Docker image from Docker Hub:
    ```bash
    docker pull dockersevinch02/online-food-store-app:1
    ```

2. Run the Docker container:
    ```bash
    docker run -d -p 9001:8080 dockersevinch02/online-food-store-app:1
    ```

### Testing Users

For testing purposes, there are two users:

1. **Manager:**
   - Username: admin
   - Password: admin123
   - Role: MANAGER

2. **Employee:**
   - Username: sevinch
   - Password: sevinch123
   - Role: EMPLOYEE

### Swagger Documentation

After running the project, you can find the Swagger documentation at [http://localhost:9001/swagger-ui/index.html](http://localhost:9001/swagger-ui/index.html).
