package org.example.StudAcc.utils;

import lombok.Data;

import java.util.Date;

@Data
public class BodyError {

    private int status;
    private String message;
    private Date timestamp;


    public BodyError(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = new Date();
    }
}
