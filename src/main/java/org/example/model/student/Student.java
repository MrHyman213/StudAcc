package org.example.model.student;

import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import org.example.model.Entry;

@Data
@Jacksonized
public class Student {

    private int id;
    private String name;
    private String surname;
    private String patronymic;
    private String caseNumber;
    private boolean akadem;
    private boolean freeVisit;

    private Boolean sex;
    private String snils;
    private String birthDate;
    private Integer graduationYear;
    private String phone;

    private Entry group;
    private Entry orderNumber;
    private Entry education;
    private Address address;

    public String getInitials(){
        return surname + " " + name + " " + patronymic;
    }
}
