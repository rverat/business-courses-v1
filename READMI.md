Business Courses API (Version 1)

This is a reactive API for managing business courses.
Description

The API provides endpoints for performing CRUD operations on business courses. It uses Spring Boot with WebFlux and MongoDB for data persistence.
Requirements

    Java 21
    MongoDB

Configuration

    Clone the repository:

    bash

git clone https://github.com/rverat/business-courses-v1.git

Navigate to the project directory:

bash

cd business-courses-v1

Build and run the application:

bash

    ./mvnw spring-boot:run

    The application will be available at http://localhost:8080.

Endpoints
Get all courses

Postman public url: https://documenter.getpostman.com/view/21762368/2s9YeN1819

http

GET /v1/api/courses

Returns a list of all courses.
Get a course by ID

http

GET /v1/api/courses/{id}

Returns a specific course based on the provided ID.
Create a new course

http

POST /v1/api/courses

Creates a new course using the data provided in the request body.
Update an existing course

http

PATCH /v1/api/courses

Updates an existing course using the data provided in the request body.
Delete a course by ID

http

DELETE /v1/api/courses/{id}

Deletes a specific course based on the provided ID.
API Documentation (Swagger)

Detailed API documentation, including examples and interactive testing, is available at:

Swagger UI
Contributions

Contributions are welcome! If you find a bug or have an improvement, please open an issue or send a pull request.
License

This project is licensed under the MIT License.