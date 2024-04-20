package org.example.DTO.student;

import lombok.Data;

@Data
public class ShortStudent {

    private int id;
    private String name;
    private String surname;
    private String patronymic;

    public String getInitials(){
        return surname + " " + name + " " + patronymic;
    }

}
