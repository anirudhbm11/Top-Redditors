package com.reddit.topusers;

import java.util.Map;
 
public class api_test {
    public static void main(String[] args) {
 
        System.out.println("Read Specific Enviornment Variable");
        System.out.println("KEY Value:- " + System.getenv("REDDIT_SECRET_TOKEN"));
 
        System.out.println("\nRead All Variables:-\n");
 
        Map <String, String> map = System.getenv();
        for (Map.Entry <String, String> entry: map.entrySet()) {
            System.out.println("Variable Name:- " + entry.getKey() + " Value:- " + entry.getValue());
        }
 
    }
}