package eu.nimble.service.example.impl;

import eu.nimble.service.example.swagger.model.Version;
import eu.nimble.service.example.swagger.api.VersionApi;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringCodegen", date = "2016-12-07T07:32:06.826Z")

@Controller
public class VersionApiController implements VersionApi {

    public ResponseEntity<Version> versionGet() {
        return new ResponseEntity<Version>(VersionFactory.create(), HttpStatus.OK);
    }

    private static class VersionFactory {
        static Version create() {
            Version v = new Version();
            v.setVersion("0.0.1");
            v.setServiceId("example");
            return v;
        }
    }
}
