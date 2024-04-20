package org.example.DTO.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.DTO.entries.District;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    private int id;
    private District district;
    private String city;
    private String street;
    private String houseNumber;
    private String flatNumber;
    private String index;
}
