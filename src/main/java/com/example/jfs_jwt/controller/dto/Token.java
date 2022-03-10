package com.example.jfs_jwt.controller.dto;

public class Token {
//    instances
    private final String accessToken;

//constructor
   public Token(String accessToken){
        this.accessToken=accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
