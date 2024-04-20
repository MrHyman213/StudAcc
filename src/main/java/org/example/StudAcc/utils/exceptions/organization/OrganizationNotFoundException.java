package org.example.StudAcc.utils.exceptions.organization;

import org.example.StudAcc.utils.exceptions.ObjectNotFoundException;

public class OrganizationNotFoundException extends ObjectNotFoundException {
    private String message;

    public OrganizationNotFoundException(String message){
        super(message);
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
