package org.example.StudAcc.controller;

import org.example.StudAcc.utils.BodyError;
import org.example.StudAcc.utils.exceptions.ObjectNotFoundException;
import org.example.StudAcc.utils.exceptions.address.AddressNotFoundException;
import org.example.StudAcc.utils.exceptions.address.DistrictNotFoundException;
import org.example.StudAcc.utils.exceptions.address.OblastNotFoundException;
import org.example.StudAcc.utils.exceptions.download.FileFormatException;
import org.example.StudAcc.utils.exceptions.download.FileNameAlreadyUsedException;
import org.example.StudAcc.utils.exceptions.download.PathNotFoundException;
import org.example.StudAcc.utils.exceptions.organization.*;
import org.example.StudAcc.utils.exceptions.student.EducationNotFoundException;
import org.example.StudAcc.utils.exceptions.student.OrderNumberNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionAdviceController {

    @ExceptionHandler({ObjectNotFoundException.class, OrderNumberNotFoundException.class,
            DisciplineNotFoundException.class, EmployeeNotFoundException.class,
            GroupNotFoundException.class, OrganizationNotFoundException.class,
            SpecializationNotFoundException.class, EducationNotFoundException.class,
            PathNotFoundException.class, AddressNotFoundException.class,
            OblastNotFoundException.class, DistrictNotFoundException.class})
    public ResponseEntity<BodyError> notFoundHandling(ObjectNotFoundException ex){
        return new ResponseEntity<>(
                new BodyError(404, ex.getMessage()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({FileFormatException.class, FileNameAlreadyUsedException.class})
    public ResponseEntity<BodyError> otherExceptionHandling(RuntimeException ex){
        return new ResponseEntity<>(
            new BodyError(400, ex.getMessage()),
            HttpStatus.BAD_REQUEST
        );
    }
}
