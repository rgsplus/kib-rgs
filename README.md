# kib-rgs

This project is an API REST for a simple registration system of `InspectionMethods` and `InspectionLists`. It was
developed using Java with Spring Boot and MongoDB.

## Index

- [Project Structure](#project-structure)
- [Project Setup Instructions](#project-setup-instructions)
  - [Important Notice](#important-notice)
  - [Creating the `application.properties` File](#creating-the-applicationproperties-file)
  - [Running MongoDB with Docker](#running-mongodb-with-docker)
  - [Running the Application](#running-the-application)
- [Swagger](#swagger)
- [Postman](#postman)
- [License](#license)

## Project Structure <a name="project-structure"></a>

The project is structured as follows:

- `src/main/java/com/kib/rgs`: Contains the main Java classes of the project.
  - `config`: Contains the configuration classes of the project.
  - `controller`: Contains the REST controllers of the project.
  - `model`:
    - `account`: Contains the `Account` domain class.
      - `dto`: Contains the DTO classes related to the `Account` domain class.
    - `file`: Contains the `KibFile` domain class.
      - `dto`: Contains the DTO classes related to the `KibFile` domain class.
    - `list`: Contains the `InspectionList` domain class.
      - `dto`: Contains the DTO classes related to the `InspectionList` domain class.
    - `method`: Contains the `InspectionMethod` domain class.
      - `dto`: Contains the DTO classes related to the `InspectionMethod` domain class.
    - `user`: Contains the `User` domain class.
      - `dto`: Contains the DTO classes related to the `User` domain class.
  - `repository`: Contains the repository interfaces of the project.
    - `listeners`: Contains the listeners of the project, now only is the `BeforeConvertEventListener`. This
      listener is responsible for setting the `createdBy`, `createdDate`, `lastModifiedBy`, and `lastModifiedDate`
      fields of the entities and sorting Sortable models.
  - `service`: Contains the service interfaces and implementations of the project.
    - `impl`: Contains the service implementations of the project.
  - `shared`: Contains the shared classes of the project.
    - `models`: Contains the shared models of the project.
    - `validators`: Contains the validator classes of the project.
- `src/main/resources`: Contains the resources of the project.
- `src/test/java/com/kib/rgs`: Contains the test classes of the project.
  - `controller`: Contains the test classes of the REST controllers of the project.
  - `models`: Contains the test classes for the models of the project.
    - `dto`: Contains the test classes for the DTO models of the project.
  - `service`: Contains the test classes of the services of the project.
  - `validators`: Contains the test classes of the validators of the project.

## Project Setup Instructions <a name="project-setup-instructions"></a>

### Important Notice <a name="important-notice"></a>

For security reasons, the `application.properties` file is ignored in our version control system (as specified in
our `.gitignore` file). This means you will need to manually create this file in your local setup to ensure the
application connects correctly to MongoDB, which is containerized using Docker as described in our `docker-compose.yml`.

### Creating the `application.properties` File <a name="creating-the-applicationproperties-file"></a>

To set up your local development environment, you need to create an `application.properties` file
under `src/main/resources/` with the following content:

```properties
# General
app.name=RECOMMENDED
# Server Errors
server.error.include-binding-errors=RECOMMENDED
server.error.include-message=RECOMMENDED
server.port=RECOMMENDED
# MongoDB
spring.data.mongodb.host=REQUIRED *
spring.data.mongodb.port=REQUIRED *
spring.data.mongodb.database=REQUIRED *
spring.data.mongodb.auto-index-creation=REQUIRED *
# Spring Main
spring.main.allow-circular-references=true REQUIRED *
# Spring Multipart
spring.servlet.multipart.enabled=REQUIRED *
spring.servlet.multipart.max-file-size=REQUIRED *
spring.servlet.multipart.max-request-size=REQUIRED *
# Spring Security OAuth2
spring.security.oauth2.resourceserver.jwt.issuer-uri=REQUIRED *
# Keycloak
keycloak.server=REQUIRED *
keycloak.realm=REQUIRED *
keycloak.client-id=REQUIRED *
keycloak.client-secret=REQUIRED *
keycloak.kib-client-id=REQUIRED *
# Mail
spring.mail.host=REQUIRED *
spring.mail.port=REQUIRED *
spring.mail.properties.mail.smtp.auth=REQUIRED *
spring.mail.properties.mail.smtp.starttls.enable=REQUIRED *
spring.mail.username=REQUIRED *
spring.mail.password=REQUIRED *
# App
app.inspection-list-service.delete-orphan-documents.cron=REQUIRED *
app.url=REQUIRED *
```

### Running MongoDB with Docker <a name="running-mongodb-with-docker"></a>

To run MongoDB using Docker, you can use the following command:

```shell
docker-compose up --build
```

### Running the Application <a name="running-the-application"></a>

To run the application, you can use the following command:

```shell
./gradlew bootRun
```

Or you can run the application using your IDE.

## Swagger <a name="swagger"></a>

To access the Swagger documentation, you can access the following URL: `http://localhost:8081/swagger-ui/index.html`.

## Postman <a name="postman"></a>

To test the API endpoints, you can import the Postman collection from the file `kib-rgs.postman-collection.json` located
in the root directory of the project.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
