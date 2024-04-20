package org.example.StudAcc.utils.exceptions.address;

public class DistrictNotFoundException extends AddressNotFoundException{

    private String message;

    public DistrictNotFoundException(String message){
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage(){
        return message;
    }
}
