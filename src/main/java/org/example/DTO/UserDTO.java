package org.example.DTO;

import lombok.Data;

import java.util.List;

@Data
public class UserDTO {

    private String login;
    private String password;
    private String email;
    private String phone;
    private List<Integer> roleList;
}
