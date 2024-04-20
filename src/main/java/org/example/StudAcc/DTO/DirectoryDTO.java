package org.example.StudAcc.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DirectoryDTO {

    private int parentId;
    private int typeId;
    private String name;
}
