package org.example.StudAcc.model.organization;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;


@Data
@Entity
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private int id;

    @ManyToOne
    @JoinColumn(name = "id_director", referencedColumnName = "id")
    private Employee director;
    @ManyToOne
    @JoinColumn(name = "id_secretary", referencedColumnName = "id")
    private Employee secretary;

    private String address;
    private String name;
    private String phone;
}
