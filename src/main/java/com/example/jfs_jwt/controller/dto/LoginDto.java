package com.example.jfs_jwt.controller.dto;

public class LoginDto {
//    instance fields
    private final String username;
    private final String password;
//constructor
  public LoginDto(String username, String password){
      this.username=username;
      this.password=password;
  }
//getters


    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
