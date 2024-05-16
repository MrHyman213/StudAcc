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

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "id_spec", referencedColumnName = "id")
    private Specialization specialization;

}
