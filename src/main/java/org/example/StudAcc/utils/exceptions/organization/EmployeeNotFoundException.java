package org.example.StudAcc.utils.exceptions.organization;

public class EmployeeNotFoundException extends OrganizationNotFoundException{
    private String message;

    public EmployeeNotFoundException(String message){
        super(message);
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
