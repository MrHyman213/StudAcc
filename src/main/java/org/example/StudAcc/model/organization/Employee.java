package org.example.StudAcc.model.organization;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String surname;
    private String patronymic;
    @ManyToMany(mappedBy = "teachers")
    @JsonIgnore
    private List<Discipline> disciplineList;

    public String getShortName(){
        return surname + " " + name.charAt(0) + ". " + patronymic.charAt(0) + ".";
    }
}
