package org.example.DTO;

import lombok.*;
import org.example.model.Entry;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TemplatesDTO extends Entry {

    private int id;
    private String name;
    private Boolean docType;

    @Override
    public String toString() {
        return name;
    }
}
