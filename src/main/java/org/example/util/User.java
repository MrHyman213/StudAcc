package org.example.util;

import lombok.Data;

@Data
public class User {

    private String token;
    private String login;
    private String password;
    private Boolean tokenExpired;

}
