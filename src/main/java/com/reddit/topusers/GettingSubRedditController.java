package com.reddit.topusers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class GettingSubRedditController {
	@Autowired
	private GettingSubReddit sub;
	
	@Autowired
	private GettingUserData ud;
	
	private String apiResult;
	
	private LinkedHashMap<String, Float> top_users;
	
	 @GetMapping("/index")
	 public void greetingForm(Model model) {
	    model.addAttribute("subreddit", sub);  
	  }
	 
	 public String getAccessToken() {
		 RestTemplate restTemplate = new RestTemplate();
		 String SECRET_TOKEN = System.getenv("REDDIT_SECRET_TOKEN");
		 String CLIENT_ID = System.getenv("REDDIT_CLIENT_ID");
	    HttpHeaders headers = new HttpHeaders();
	    headers.setBasicAuth(CLIENT_ID, SECRET_TOKEN);
	    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
	    headers.put("User-Agent",
	            Collections.singletonList("web.com.reddit.topusers:v1.0 (by /u/anirudhbm11)"));
	    String body = "grant_type=client_credentials";
	    HttpEntity<String> request
	            = new HttpEntity<>(body, headers);
	    String authUrl = "https://www.reddit.com/api/v1/access_token";
	    ResponseEntity<String> response = restTemplate.postForEntity(
	            authUrl, request, String.class);
	    ObjectMapper mapper = new ObjectMapper();
	    Map<String, Object> map = new HashMap<>();
	    try {
	    	map = mapper.readValue(response.getBody(), Map.class);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    
	    System.out.println(response.getBody());
	    return String.valueOf(map.get("access_token"));
	 }
	 
	
	  @PostMapping("/index") 
	  public String TopUsers(@ModelAttribute GettingSubReddit sub,Model model) {
		String limit = "25";
		String day = "month";
	    model.addAttribute("subreddit", sub);
	    ResponseEntity<String> response;
	    
	    String final_subreddit = sub.getSubreddit_path();
    
	    RestTemplate restTemplate = new RestTemplate();
	    HttpHeaders headers = new HttpHeaders();
	    String authToken = getAccessToken();
	    headers.setBearerAuth(authToken);
	    headers.put("User-Agent",
	            Collections.singletonList("web.com.reddit.topusers:v1.0 (by /u/anirudhbm11)"));
	    HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
	    String url = String.format("https://oauth.reddit.com/r/%s/top?t=%s&limit=%s", final_subreddit,day,limit);
	    
	    try {
	    	response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
	    }catch(Exception e){
	    	return "invalid_subreddit";
	    }
	    
	    if(response.getStatusCode() == HttpStatus.OK) {
	    	System.out.println("Query successfull");
	    }else {
	    	System.out.println("Enter a valid subreddit");
	    }
	    
	    apiResult = response.getBody();
	    
	    sub.setSubreddit_data(apiResult);
	    
	    ArrayList<String> authors = sub.getSubreddit_data();
	    
	    ud.setAuthors(authors);
	    ud.setSubreddit(final_subreddit);
	    
	    ud.gettingUsersData(authToken);
	    top_users = ud.sortedUsers();
	    
	    for(Map.Entry<String, Float> e: top_users.entrySet()) {
			System.out.println(e.getKey() + " " + e.getValue());
		}
	    
	    sub.setTopUsers(top_users);
	    
	    model.addAttribute("enteredSubreddit", sub.getSubreddit_path());
	    model.addAttribute("topUsers", sub.getTopUsers());
	    
	    return "result";
	  }

  
}