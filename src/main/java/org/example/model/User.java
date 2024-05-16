package org.example.model;

import lombok.Data;

import java.util.List;

@Data
public class User {

    private int id;
    private String token;
    private String username;
    private String password;
    private String email;
    private String phone;
    private List<Entry> roleList;
    private Boolean tokenExpired;

    @Override
    public String toString() {
        return username;
    }
}
