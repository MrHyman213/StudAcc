package org.example.StudAcc.model.student.parent;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.example.StudAcc.model.student.Student;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class Parent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String surname;
    private String lastName;
    private String phone;
    private boolean sex;
    @Column(name = "birth_year")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private Date birthYear;

    @ManyToOne
    @JoinColumn(name = "id_status", referencedColumnName = "id")
    private Status status;

    @ManyToMany
    @JoinTable(
            name = "student_parent",
            joinColumns = @JoinColumn(name = "id_parent", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_student", referencedColumnName = "id")
    )
    @JsonIgnore
    private List<Student> studentList;
}
