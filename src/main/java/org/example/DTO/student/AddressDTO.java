package org.example.DTO.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {

    private int idDistrict;
    private String city;
    private String street;
    private String houseNumber;
    private String flatNumber;
    private String index;
}