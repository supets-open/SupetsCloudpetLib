package com.supets.coredata;

import java.io.Serializable;

public class BaseDTO implements Serializable{

    private static final long serialVersionUID = 1L;

    public int  code;
    public String msg;
    public String alert;

    public boolean isSucceeded() {
        return (code == 200);
    }
    
    public void updateData() {}
}
