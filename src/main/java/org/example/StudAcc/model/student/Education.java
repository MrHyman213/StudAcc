package org.example.StudAcc.model.student;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Education {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    @OneToMany(mappedBy = "education")
    @JsonIgnore
    private List<Student> studentList;

    public Education(String name) {
        this.name = name;
        this.studentList = Collections.emptyList();
    }

    public Education(String name, List<Student> studentList) {
        this.name = name;
        this.studentList = studentList;
    }
}
