package com.reddit.topusers;

import java.util.Collections;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ApiController {
	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String result(String url, String authToken) {
		RestTemplate restTemplate = new RestTemplate();
	    HttpHeaders headers = new HttpHeaders();
	    headers.setBearerAuth(authToken);
	    headers.put("User-Agent",
	            Collections.singletonList("web.com.reddit.topusers:v1.0 (by /u/anirudhbm11)"));
	    HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
	    ResponseEntity<String> response
	            = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
	    
	    if(response.getStatusCode() == HttpStatus.OK) {
	    	System.out.println("Query successfull");
	    }else {
	    	System.out.println("Enter a valid subreddit");
	    }
	    
	    return response.getBody();
	}
}
