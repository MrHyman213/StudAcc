package org.example.DTO.entries;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class District implements Entry{

    private int id;
    private String name;
    private Region region;
}
