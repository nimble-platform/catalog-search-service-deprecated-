package eu.nimble.service.catalog.search.impl;

import eu.nimble.service.catalog.search.swagger.model.Example;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;

public class ExampleControllerTest {

    private ExampleController exc;

    @Before
    public void setUp() {
        exc = new ExampleController();

    }

    @Test
    public void testGet() {
        ResponseEntity<Example> ex = exc.getExample();
        assertEquals(46, ex.getBody().getProp2().longValue());

    }

    @Test
    public void testAdd() {
        ResponseEntity<Void> ex = exc.addExample(ExampleFactory.create());
        assertEquals(HttpStatus.OK, ex.getStatusCode());

        ResponseEntity<Void> ex2 = exc.addExample(null);
        assertEquals(HttpStatus.BAD_REQUEST, ex2.getStatusCode());
    }

    @After
    public void tearDown() {
    }

    private static class ExampleFactory {
        static Example create() {
            Example e = new Example();
            e.setProp1("Marc Marquez");
            e.setProp2(93);
            return e;
        }
    }


}
