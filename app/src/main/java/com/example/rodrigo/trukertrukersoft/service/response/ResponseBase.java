package com.example.rodrigo.trukertrukersoft.service.response;

/**
 * Created by Rodrigo on 29/03/2017.
 */

public class ResponseBase {
    private boolean IsSuccess;
    private String Message;

    public ResponseBase() {
    }

    public ResponseBase(boolean isSuccess, String message) {
        IsSuccess = isSuccess;
        Message = message;
    }

    public boolean isSuccess() {
        return IsSuccess;
    }

    public void setSuccess(boolean success) {
        IsSuccess = success;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
