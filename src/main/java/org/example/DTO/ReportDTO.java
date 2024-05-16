package org.example.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {

    private String dateEvent;
    private List<Integer> employeeIdList;
    private Integer disciplineId;
    private Integer studentId;
    private String semester;
    private String yearStudy;
    private String dateStart;
    private String dateEnd;
    private String numberProtocol;
}
