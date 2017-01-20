# Microservice Example using Java 8
**ServiceID**: java-microservice-example

Example implementation for a microservice written in Java and the Spring Boot framework.
  
## Configuration

Base configuration can be found at src/main/resources/application.properties and bootstrap.yml.
[Spring Cloud Config](https://cloud.spring.io/spring-cloud-config/) is used for central configuration management. A central configuration is hosted on [https://github.com/nimble-platform/cloud-config](https://github.com/nimble-platform/cloud-config)
and injected during startup.

## Swagger

The Example API is designed using the [swagger.io editor](http://editor.swagger.io) (file: src/main/resources/api.yml) and the code generator for the Spring framework. 
The Maven plugin (swagger-codegen-maven-plugin) is used to generate defined interfaces automatically in each Maven build.

## How-to

### Service build and startup

 ```bash
 # standalone
 mvn clean spring-boot:run
 
 # in docker environment from core cloud infrastructure using 8085 as internal port
 mvn clean package docker:build -P docker
 docker run -p 8081:8085 nimbleplatform/java-microservice-example
 ```
 The according Dockerfile can be found at src/main/docker/Dockerfile.
 
### Example requests
 ```bash
 # get
 curl http://localhost:8081/example
 
 # post
 curl -H "Content-Type: application/json" -d '{"prop1":"Jorge Lorenzo","prop2":99}' http://localhost:8081/example
 ```
 ---
The project leading to this application has received funding from the European Union’s Horizon 2020 research and innovation programme under grant agreement No 723810.
