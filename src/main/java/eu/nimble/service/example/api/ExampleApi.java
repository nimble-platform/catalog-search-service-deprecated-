package eu.nimble.service.example.api;

import eu.nimble.service.example.model.Example;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringCodegen", date = "2016-12-12T13:45:55.427Z")

@Api(value = "example", description = "the example API")
public interface ExampleApi {

    @ApiOperation(value = "", notes = "post an example object ", response = Void.class, tags={  })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "example post successful", response = Void.class),
            @ApiResponse(code = 400, message = "example post not successful, bad request.", response = Void.class) })
    @RequestMapping(value = "/example",
            consumes = { "application/json" },
            method = RequestMethod.POST)
    ResponseEntity<Void> addExample(@ApiParam(value = "Example object that needs to be added"  ) @RequestBody Example body);


    @ApiOperation(value = "", notes = "get an example object ", response = Example.class, tags={  })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful response", response = Example.class) })
    @RequestMapping(value = "/example",
            produces = { "application/json" },
            method = RequestMethod.GET)
    ResponseEntity<Example> getExample();

}
