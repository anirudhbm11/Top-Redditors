package com.reddit.topusers;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class GettingSubReddit {
	
  private String subreddit_path;
  private String subreddit_data;
  private ArrayList<String> authors;
  private LinkedHashMap<String, Float> topUsers;

  public LinkedHashMap<String, Float> getTopUsers() {
	return topUsers;
  }
  
  public void setTopUsers(LinkedHashMap<String, Float> topUsers) {
	  this.topUsers = topUsers;
  }
  
  public ArrayList<String> getAuthors() {
	  return authors;
  }
  
  public void setAuthors(ArrayList<String> authors) {
	  this.authors = authors;
  }
  
  public String getSubreddit_path() {
	  return subreddit_path;
  }
  
  public void setSubreddit_path(String subreddit_path) {
	  this.subreddit_path = subreddit_path.toLowerCase();
  }
  
  public ArrayList<String> getSubreddit_data() {
	this.authors = new ArrayList<String>();
	JSONObject jo = new JSONObject(this.subreddit_data);
	JSONObject data = (JSONObject) jo.get("data");
	JSONArray children = (JSONArray)data.get("children");
	for(int i=0;i<children.length();i++) {
		JSONObject final_data = (JSONObject)children.get(i);
		JSONObject author_data = (JSONObject)final_data.get("data");
		String author = (String)author_data.get("author");
			this.authors.add(author);
		}
		
		return authors;
	}
	
	public void setSubreddit_data(String subreddit_data) {
		this.subreddit_data = subreddit_data;
	}
	

}