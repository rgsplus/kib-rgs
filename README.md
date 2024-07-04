# kib-rgs

## Project Setup Instructions

### Important Notice

For security reasons, the `application.properties` file is ignored in our version control system (as specified in our `.gitignore` file). This means you will need to manually create this file in your local setup to ensure the application connects correctly to MongoDB, which is containerized using Docker as described in our `docker-compose.yml`.

### Creating the `application.properties` File

To set up your local development environment, you need to create an `application.properties` file under `src/main/resources/` with the following content:

```properties
# MongoDB Configuration
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.database=kib-rgs
```
