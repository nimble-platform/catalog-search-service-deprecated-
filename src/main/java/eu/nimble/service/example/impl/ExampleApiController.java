package eu.nimble.service.example.impl;

import eu.nimble.service.example.api.ExampleApi;
import eu.nimble.service.example.model.Example;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringCodegen", date = "2016-12-12T13:45:55.427Z")

@Controller
public class ExampleApiController implements ExampleApi {

    private static Logger log = LoggerFactory.getLogger(ExampleApiController.class);

    public ResponseEntity<Void> addExample(@ApiParam(value = "Example object that needs to be added"  ) @RequestBody Example body) {
        // do some magic!
        log.info("example added: {}", body);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    public ResponseEntity<Example> getExample() {
        final Example example = ExampleFactory.create();
        // do some magic!
        log.info("example returned: {}", example);
        return new ResponseEntity<Example>(example, HttpStatus.OK);
    }

    private static class ExampleFactory {
        static Example create() {
            Example e = new Example();
            e.setProp1("Valentino Rossi");
            e.setProp2(46);
            return e;
        }
    }
}
