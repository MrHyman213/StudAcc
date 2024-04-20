package org.example.StudAcc.DTO.excel;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Student {

    private String name;
    private String surname;
    private String patronymic;
    private LocalDate birthDate;
    private Boolean sex;
    private String snils;
    private String group;
    private String orderNumber; // id_order_number Номер приказа
    private String dateOrder; // Дата приказа
    private int graduationYear;
    private String education;
    private String address;

    public void setSex(String sex){
        this.sex = String.valueOf(sex.charAt(0)).equalsIgnoreCase("ж");
    }

}
