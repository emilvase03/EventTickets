# EventTickets

EventTickets is a JavaFX desktop application for managing events and tickets. It provides user authentication, role-based dashboards (admin and coordinator), event and ticket management, and printable ticket output.

## Features
- User login with role-based views (Admin / Coordinator)
- Create, edit and list events
- Create and print tickets for events
- Responsive JavaFX UI built with MaterialFX components

## Tech stack
- Java 24
- JavaFX 21
- MaterialFX
- Microsoft SQL Server (JDBC)
- Maven (build and dependency management)

## Prerequisites
- JDK 24 installed and JAVA_HOME configured
- Maven (the project includes the Maven wrapper `mvnw`)

## Installation
Download or clone the repository and change into its directory, then build with the included Maven wrapper:

```bash
./mvnw clean package
```

## Running the application
Recommended: open the project in your IDE (IntelliJ IDEA, Eclipse, etc.) and run the `dk.easv.eventtickets.Launcher` main class.

Or run with the Maven JavaFX plugin:

```bash
./mvnw clean javafx:run
```

If you prefer to run a packaged jar, launch with the JavaFX SDK on the module path:

```bash
java --module-path /path/to/javafx-sdk-21/lib --add-modules javafx.controls,javafx.fxml -jar target/EventTickets-1.0-SNAPSHOT.jar
```

Adjust the module path to match your local JavaFX SDK location.

## Configuration
Database connection settings (JDBC URL, username, password) should be provided through your environment or a configuration mechanism used by the application. The project expects a Microsoft SQL Server JDBC connection.

## Examples
- Log in as an Admin to create and manage events and coordinators.
- Log in as a Coordinator to create tickets for events and open the Print Tickets view to generate printable tickets.

## Running tests
Run unit tests with Maven:

```bash
./mvnw test
```

## Project structure (high level)
- `src/main/java` - application code (controllers, business logic, data access, models)
- `src/main/resources` - FXML views, CSS, icons and components
- `pom.xml` - Maven build configuration

