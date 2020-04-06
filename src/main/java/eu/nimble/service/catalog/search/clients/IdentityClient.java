package eu.nimble.service.catalog.search.clients;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "identity-service", url = "${nimble.identity.url}", fallback = IdentityClientFallback.class)
	public interface IdentityClient {
	  @RequestMapping(value = "/party_by_person/{personId}", produces = {"application/json"}, method = RequestMethod.GET)
	    Object getPerson(Long personId);
	}

