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
public class ExampleController implements ExampleApi {

    private static Logger log = LoggerFactory.getLogger(ExampleController.class);

    public ResponseEntity<Void> addExample(@ApiParam(value = "Example object that needs to be added"  ) @RequestBody Example body) {
        if (body == null || body.getProp1() == null || body.getProp2() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else {
            log.info("example added: {}", body);
            return ResponseEntity.ok(null);
        }
    }

    public ResponseEntity<Example> getExample() {
        final Example example = ExampleFactory.create();
        log.info("example returned: {}", example);
        return ResponseEntity.ok(example);
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
