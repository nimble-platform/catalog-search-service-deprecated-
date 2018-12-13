package eu.nimble.service.catalog.search.clients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.hibernate.validator.internal.util.privilegedactions.GetMethod;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
	public class IdentityClientFallback implements IdentityClient {

	
	
	private static final String PARTY_BY_PERSON = "party_by_person/";
	@Value("${nimble.identity.url}")
	private String url;

		@Override
		public Object getPerson(Long personId) {
			// Create an instance of HttpClient.
		    HttpClient client = new  DefaultHttpClient();
		    String url = "";
		    if (this.url.charAt(this.url.length()-1) == '/'){
		    	url = this.url + PARTY_BY_PERSON;
		    }
		    else{
		    	url = this.url +"/" +PARTY_BY_PERSON;
		    }
		    
		    

		    // Create a method instance.
		    HttpGet request = new HttpGet(url);
		    StringBuffer stringBuffer = new StringBuffer();
		    try {
				HttpResponse response = client.execute(request);
				
				// Get the response
				BufferedReader rd = new BufferedReader
				    (new InputStreamReader(
				    response.getEntity().getContent()));

				String line = "";
				while ((line = rd.readLine()) != null) {
					stringBuffer.append(line);
				}
				return stringBuffer.toString();
				
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	}

