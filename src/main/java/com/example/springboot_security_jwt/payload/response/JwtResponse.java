package com.example.springboot_security_jwt.payload.response;

import java.util.List;

public class JwtResponse {
    private String token;
    private String type = "Bearer ";
    private int userId;
    private String userName;
    private String email;
    private String phone;
    private List<String> listRoles;

    public JwtResponse(String token, int userId, String userName, String email, String phone, List<String> listRoles) {
        this.token = token;
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.phone = phone;
        this.listRoles = listRoles;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<String> getListRoles() {
        return listRoles;
    }

    public void setListRoles(List<String> listRoles) {
        this.listRoles = listRoles;
    }
}
