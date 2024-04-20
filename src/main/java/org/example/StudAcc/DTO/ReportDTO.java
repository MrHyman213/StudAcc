package org.example.StudAcc.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class ReportDTO {

    private Date dateEvent;
    private List<Integer> employeeIdList;
    private Integer disciplineId;
    private Integer studentId;
    private String semester;
    private String yearStudy;
    private Date dateStart;
    private Date dateEnd;
}
