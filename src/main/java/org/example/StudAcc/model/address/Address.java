package org.example.StudAcc.model.address;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String city;
    private String street;
    private String houseNumber;
    private String flatNumber;
    @Column(name = "`index`")
    private String index;

    @ManyToOne
    @JoinColumn(name = "id_district", referencedColumnName = "id")
    private District district;

    public String toString(){
        return city + " " + street + " " + houseNumber + " " + flatNumber;
    }
}
