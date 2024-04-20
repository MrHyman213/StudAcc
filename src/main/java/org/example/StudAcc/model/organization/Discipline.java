package org.example.StudAcc.model.organization;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Discipline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @ManyToMany
    @JoinTable(
            name = "discipline_teacher",
            joinColumns = @JoinColumn(name = "id_discipline", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_teacher", referencedColumnName = "id")
    )
    @JsonIgnore
    private List<Employee> teachers;

    @ManyToMany
    @JoinTable(
            name = "specialization_discipline",
            joinColumns = @JoinColumn(name = "id_discipline", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_specialization", referencedColumnName = "id")
    )
    @JsonIgnore
    private List<Specialization> specializationList;

}
