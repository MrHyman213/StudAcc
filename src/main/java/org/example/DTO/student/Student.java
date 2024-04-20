package org.example.DTO.student;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;
import org.example.DTO.entries.Education;
import org.example.DTO.entries.Group;
import org.example.DTO.entries.OrderNumber;

import java.util.Objects;

@Data
@Jacksonized
public class Student {

    private int id;
    private String name;
    private String surname;
    private String patronymic;
    private String caseNumber;
    private boolean akadem;
    private boolean free;

    private Boolean sex;
    private String snils;
    private String birthDate;
    private Integer graduationYear;
    private String phone;

    private Group group;
    private OrderNumber orderNumber;
    private Education education;
    private Address address;

    public String getInitials(){
        return surname + " " + name + " " + patronymic;
    }
}
