package org.example.StudAcc.utils.exceptions.address;

public class AddressNotFoundException extends RuntimeException{

    private String message;

    public AddressNotFoundException(String message){
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage(){
        return message;
    }
}
