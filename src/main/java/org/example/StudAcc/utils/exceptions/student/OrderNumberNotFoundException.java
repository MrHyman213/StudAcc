package org.example.StudAcc.utils.exceptions.student;

import org.example.StudAcc.utils.exceptions.ObjectNotFoundException;
import org.springframework.core.annotation.Order;

public class OrderNumberNotFoundException extends ObjectNotFoundException {

    private String message;

    public OrderNumberNotFoundException(String message){
        super(message);
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
