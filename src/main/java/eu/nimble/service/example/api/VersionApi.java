package eu.nimble.service.example.api;

import eu.nimble.service.example.model.Version;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringCodegen", date = "2016-12-07T07:32:06.826Z")

@Api(value = "version", description = "the version API")
public interface VersionApi {

    @ApiOperation(value = "", notes = "get the name and version string ", response = Version.class, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response", response = Version.class) })
    @RequestMapping(value = "/version",
        produces = { "application/json" },
        method = RequestMethod.GET)
    ResponseEntity<Version> versionGet();

}
