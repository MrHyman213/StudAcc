package org.example.StudAcc.model.organization;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class Specialization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    @OneToMany(mappedBy = "specialization")
    @JsonIgnore
    private List<Discipline> disciplineList;

    @OneToMany(mappedBy = "specialization")
    @JsonIgnore
    private List<Group> groupList;

    public Specialization(String name, List<Discipline> disciplineList, List<Group> groupList) {
        this.name = name;
        this.disciplineList = disciplineList;
        this.groupList = groupList;
    }
}
