package org.example.StudAcc.utils.exceptions.download;

public class FileFormatException extends RuntimeException{

    private String message;

    public FileFormatException(String message){
        super(message);
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
