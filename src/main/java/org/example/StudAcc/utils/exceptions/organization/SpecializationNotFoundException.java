package org.example.StudAcc.utils.exceptions.organization;

public class SpecializationNotFoundException extends OrganizationNotFoundException{
    private String message;

    public SpecializationNotFoundException(String message){
        super(message);
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
