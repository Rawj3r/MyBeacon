package com.equidais.mybeacon.model;

/**
 * Created by daydreamer on 8/12/2015.
 */
public class LoginResult {
    public int UserID;
    public boolean IsSuccess;
    public String Message;

    public LoginResult(){
        UserID = 0;
        IsSuccess = false;
        Message = "";
    }
}
