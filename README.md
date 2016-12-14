# Microservice Example using Java 8

Example implementation for a microservice written in Java and the Spring Boot framework.  

The Example API was designed using the [swagger.io editor](http://editor.swagger.io) (file: api.yml) and the code generator for the Spring framework.

## How-to

### Service startup

 ```bash
 # standalone
 mvn clean spring-boot:run
 
 # in docker environment from core cloud infrastructure
 ```
 
### Example requests
 ```bash
 # get
 curl http://localhost:8081/example
 
 # post
 curl -H "Content-Type: application/json" -d '{"prop1":"Jorge Lorenzo","prop2":99}' http://localhost:8081/example
 ```
