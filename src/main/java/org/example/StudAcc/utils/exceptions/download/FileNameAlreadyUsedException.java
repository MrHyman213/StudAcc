package org.example.StudAcc.utils.exceptions.download;

public class FileNameAlreadyUsedException extends RuntimeException{

    private String message;

    public FileNameAlreadyUsedException(String message){
        super(message);
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
