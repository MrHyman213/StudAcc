package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class District {

    private int id;
    private String name;
    private Entry region;

    @Override
    public String toString() {
        return name;
    }
}
