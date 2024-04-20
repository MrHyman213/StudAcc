package org.example.StudAcc.utils.exceptions.organization;

import java.util.Collection;

public class GroupNotFoundException extends OrganizationNotFoundException{

    private String message;

    public GroupNotFoundException(String message){
        super(message);
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
