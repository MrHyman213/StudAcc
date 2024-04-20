package org.example.StudAcc.utils.exceptions.address;

public class OblastNotFoundException extends AddressNotFoundException{

    private String message;

    public OblastNotFoundException(String message){
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage(){
        return message;
    }
}
