package org.example.model.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.Entry;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    private int id;
    private Entry district;
    private String city;
    private String street;
    private String houseNumber;
    private String flatNumber;
    private String index;

    public Address(int id){
        this.id = id;
    }
}
