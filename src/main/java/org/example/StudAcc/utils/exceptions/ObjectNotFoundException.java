package org.example.StudAcc.utils.exceptions;

public class ObjectNotFoundException extends RuntimeException{

    private String message;

    public ObjectNotFoundException(String message){
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage(){
        return message;
    }


}
