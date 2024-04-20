package org.example.StudAcc.utils.exceptions.student;

import org.example.StudAcc.utils.exceptions.ObjectNotFoundException;

public class EducationNotFoundException extends ObjectNotFoundException {
    private String message;

    public EducationNotFoundException(String message){
        super(message);
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
