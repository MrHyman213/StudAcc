package org.example.StudAcc.DTO.acc;

import lombok.Data;

import java.util.List;

@Data
public class RegistrationUserDto {

    private String login;
    private String password;
    private String email;
    private String phone;
    private List<Integer> roleList;
}
