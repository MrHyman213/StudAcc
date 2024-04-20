package org.example.StudAcc.DTO.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShortStudentDTO {

    private int id;
    private String name;
    private String surname;
    private String patronymic;
}
