package eu.nimble.service.example.impl;

import eu.nimble.service.example.swagger.api.ExampleApi;
import eu.nimble.service.example.swagger.model.Example;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class ApiController implements ExampleApi {

    private static Logger log = LoggerFactory.getLogger(ApiController.class);

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
