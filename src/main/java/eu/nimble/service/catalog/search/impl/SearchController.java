package eu.nimble.service.catalog.search.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;

import eu.nimble.service.catalog.search.impl.dao.InputParameter;
import eu.nimble.service.catalog.search.mediator.MediatorResult;
import eu.nimble.service.catalog.search.mediator.MediatorWebservice;

@Controller
public class SearchController {

	 @Value("${nimble.shared.property.config.d:C:/development/mimble/config.xml}")
	private Object configPath;

	@RequestMapping(value = "/query", method = RequestMethod.GET)
    HttpEntity<Object> query(@RequestParam("query") String query)
    {
	    {
	    	Gson gson = new Gson();
	    	InputParameter parameter = gson.fromJson(query, InputParameter.class);
	        MediatorWebservice service = new MediatorWebservice(configPath,parameter);
	        MediatorResult result = service.query(null, false);
	       
	        return new ResponseEntity<Object>(result.toOutput(parameter.getTypeOfOutput()), HttpStatus.OK);
	    }
    }
	
}
