package org.example.StudAcc.DTO.student;

import lombok.Data;

import java.util.Date;

@Data
public class StudentDTO {

    private int addressId;
    private int groupId;
    private int orderNumberId;
    private int educationId;
    private int graduationYear;
    private String name;
    private String surname;
    private String patronymic;
    private String phone;
    private String snils;
    private String caseNumber;
    private boolean akadem;
    private boolean sex;
    private boolean freeVisit;
    private Date birthDate;
}
