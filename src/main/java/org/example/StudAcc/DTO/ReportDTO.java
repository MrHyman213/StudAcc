package org.example.StudAcc.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class ReportDTO {

    private LocalDate dateEvent;
    private List<Integer> employeeIdList;
    private Integer disciplineId;
    private Integer studentId;
    private String semester;
    private String yearStudy;
    private String numberProtocol;
    private LocalDate dateStart;
    private LocalDate dateEnd;
}
