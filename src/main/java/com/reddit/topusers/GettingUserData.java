package com.reddit.topusers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GettingUserData {
	private ArrayList<String> authors;
	private String apiResult;
	private String subreddit;
	private Map<String, Float> user_votes;
	
	public Map<String, Float> getUser_votes() {
		return user_votes;
	}

	public void setUser_votes(Map<String, Float> user_votes) {
		this.user_votes = user_votes;
	}

	public String getSubreddit() {
		return subreddit;
	}

	public void setSubreddit(String subreddit) {
		this.subreddit = subreddit;
	}

	@Autowired
	ApiController ac;

	public ArrayList<String> getAuthors() {
		return authors;
	}

	public void setAuthors(ArrayList<String> authors) {
		this.authors = authors;
	}
	
	public void gettingUsersData(String authToken) {
		String author;
		String limit = "25";
		String day = "month";
		String sort = "top";
		float average_upvotes;
		this.user_votes = new HashMap<>();
		
		for(int i=0;i<authors.size();i++) {
			author = authors.get(i);
			String url = String.format("https://oauth.reddit.com/user/%s/comments?t=%s&sort=%s&limit=%s", author,day,sort,limit);
		    this.apiResult = ac.result(url,authToken);
		    JSONArray children = getChildrens();
		    average_upvotes = getUpvotes(children);
		    this.user_votes.put(author, average_upvotes);
		}
	    
	}
	
	public LinkedHashMap<String, Float> sortedUsers(){
		LinkedHashMap<String, Float> sortusers = new LinkedHashMap<>();
		ArrayList<Map.Entry<String,Float>> users_list = new ArrayList<>();
		for(Map.Entry<String, Float> e: this.user_votes.entrySet()) {
			users_list.add(e);
		}
		
		Comparator<Map.Entry<String, Float>> average_comparator = new Comparator<Map.Entry<String, Float>>() {
		            
		            @Override
		            public int compare(Map.Entry<String, Float> e1, Map.Entry<String, Float> e2) {
		            	Float v1 = e1.getValue();
		            	Float v2 = e2.getValue();
		                return ~v1.compareTo(v2);
		            }
		    };
		
		Collections.sort(users_list,average_comparator);
		
		for(Map.Entry<String, Float> e: users_list) {
			sortusers.put(e.getKey(), e.getValue());
		}
		
		return sortusers;
	}
	
	public JSONArray getChildrens() {
		JSONObject jo = new JSONObject(this.apiResult);
		JSONObject data = (JSONObject) jo.get("data");
		JSONArray children = (JSONArray)data.get("children");
		
		return children;
	}
	
	public float getUpvotes(JSONArray children) {
		int total_upvotes = 0;
		int total_valid_sub = 1;
		float avg_upvotes = 0;
		for(int i=0;i<children.length();i++) {
			JSONObject final_data = (JSONObject)children.get(i);
			JSONObject user_data = (JSONObject)final_data.get("data");
			String comment_subreddit = (String)user_data.get("subreddit");
			
			if(comment_subreddit.toLowerCase().equals(subreddit.toLowerCase())) {
				Integer upvotes = (Integer)user_data.get("ups");
				total_upvotes += upvotes;
				total_valid_sub++;
			}
		}
		
		avg_upvotes = total_upvotes / total_valid_sub;
		
		
		return avg_upvotes;
	}
	
}
