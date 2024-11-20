Sport Events Management Application
Overview
This is a Spring Boot application designed to manage sport events and sport types. It provides a RESTful API that allows clients to create, retrieve, and update sport events, as well as retrieve a list of available sport types. The application uses an in-memory H2 database and initializes it with a predefined list of sport types upon startup.

Features
Create Sport Events: Allows the creation of new sport events with validation on input data.
Retrieve Sport Events: Fetches a list of sport events with optional filtering by status and sport type.
Retrieve Sport Event by ID: Retrieves detailed information about a specific sport event.
Change Sport Event Status: Updates the status of a sport event with validation on status transitions.
List Sport Types: Provides a list of all available sport types.
Technologies Used
Java: Programming language used for development.
Spring Boot: Framework for building the application.
Spring Data JPA: For data persistence and repository management.
H2 Database: In-memory database for testing and development.
Maven: Build and dependency management tool.
Lombok: For reducing boilerplate code.
Jackson: For JSON processing.
SLF4J & Logback: For logging.
Getting Started
Prerequisites
Java Development Kit (JDK) 17 or higher.
Maven 3.6 or higher.
Installation

mvn spring-boot:run
The application will start on http://localhost:8080.


Username: sa
Password: (leave blank)
API Documentation
Base URL
All API endpoints are prefixed with /api.

Sport Events Endpoints
Create a Sport Event
URL: POST /api/sport-events

Description: Creates a new sport event.

Request Body:

json
Copy code
{
"name": "Championship Match",
"sportTypeId": 1,
"startTime": "2024-12-01T15:00:00"
}
Validation:

name: Must not be blank.
sportTypeId: Must be a valid sport type ID.
startTime: Must be a future date and time.
Response:

Status: 201 Created

Body:

json
Copy code
{
"id": 1,
"name": "Championship Match",
"sportTypeId": 1,
"sportTypeName": "FOOTBALL",
"status": "INACTIVE",
"startTime": "2024-12-01T15:00:00"
}
Get All Sport Events
URL: GET /api/sport-events
Description: Retrieves a list of sport events with optional filters.
Query Parameters:
status (optional): Filter events by status (INACTIVE, ACTIVE, FINISHED).
sportType (optional): Filter events by sport type name (e.g., FOOTBALL).
Response:
Status: 200 OK
Body: Array of sport event objects.
Get Sport Event by ID
URL: GET /api/sport-events/{id}
Description: Retrieves a specific sport event by its ID.
Response:
Status: 200 OK

Body:

json
Copy code
{
"id": 1,
"name": "Championship Match",
"sportTypeId": 1,
"sportTypeName": "FOOTBALL",
"status": "INACTIVE",
"startTime": "2024-12-01T15:00:00"
}
Change Sport Event Status
URL: PUT /api/sport-events/{id}/status

Description: Changes the status of a sport event.

Request Body:

json
Copy code
{
"status": "ACTIVE"
}
Validation:

status: Must be one of INACTIVE, ACTIVE, FINISHED.
Status transitions are validated according to business rules.
Response:

Status: 204 No Content
Sport Types Endpoint
Get All Sport Types
URL: GET /api/sport-types
Description: Retrieves a list of all available sport types.
Response:
Status: 200 OK

Body: Array of sport type objects.

json
Copy code
[
{
"id": 1,
"name": "FOOTBALL"
},
{
"id": 2,
"name": "BASKETBALL"
},
...
]
Data Initialization
Upon startup, the application initializes the H2 database with a predefined list of sport types from the sport-types.json file located in the src/main/resources directory.

Error Handling
The application includes global exception handling to provide meaningful error responses.

400 Bad Request: Returned when the request is invalid or fails validation.
404 Not Found: Returned when a requested resource is not found.
500 Internal Server Error: Returned when an unexpected error occurs.
Example Error Response:

json
Copy code
{
"error": "Invalid sport type",
"message": "The provided sport type ID does not exist."
}
Logging
Logging is configured using Logback with SLF4J. The logging level is set to:

DEBUG for com.example.sportevents package.
INFO for the root logger and other packages.
Project Structure
config: Configuration classes, such as ClockConfiguration and DataInitializer.
controller: REST controllers for handling API requests.
dto: Data Transfer Objects for transferring data between layers.
exception: Custom exception classes and global exception handler.
model: Entity classes and enums representing the data model.
repository: Spring Data JPA repositories for data access.
service: Service classes containing business logic.
util: Utility classes, such as DateTimeUtils.
resources: Contains application.yml for configuration and sport-types.json for data initialization.
Validation Rules
SportEventDto:
name: Must not be blank.
sportTypeId: Must not be null and must reference a valid sport type.
startTime: Must be a future date and time.
StatusChangeRequest:
status: Must not be blank and must be a valid status.
Business Rules for Status Transitions
From INACTIVE to ACTIVE:
Allowed only if the current status is INACTIVE.
The startTime must be in the future.
From ACTIVE to FINISHED:
Allowed only if the current status is ACTIVE.
Finished Events:
Cannot change the status of events that are already FINISHED.