package org.example.StudAcc.utils.exceptions.organization;

public class DisciplineNotFoundException extends OrganizationNotFoundException{

    private String message;

    public DisciplineNotFoundException(String message){
        super(message);
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
