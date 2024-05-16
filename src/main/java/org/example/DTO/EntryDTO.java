package org.example.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntryDTO {

    private int parentId;
    private String name;

    public EntryDTO(String name) {
        this.name = name;
    }
}
