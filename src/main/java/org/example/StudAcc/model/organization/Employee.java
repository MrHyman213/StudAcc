package org.example.StudAcc.model.organization;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    public String getShortName(){
        String[] initials = name.split(" ");
        try {
            return initials[0] + " " + initials[1].charAt(0) + ". " + initials[2].charAt(0) + ".";
        } catch (IndexOutOfBoundsException e){
            return name;
        }
    }

    public Employee(String name) {
        this.name = name;
    }
}

