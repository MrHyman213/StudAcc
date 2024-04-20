package org.example.StudAcc.utils.exceptions.download;

public class PathNotFoundException extends RuntimeException{

    private String message;

    public PathNotFoundException(String message){
        super(message);
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
