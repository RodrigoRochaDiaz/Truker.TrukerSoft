package com.example.rodrigo.trukertrukersoft.service.response;

/**
 * Created by Rodrigo on 29/03/2017.
 */

public class UserRegistryResponse extends ResponseBase {
    private int UserId;

    public UserRegistryResponse() {
    }

    public UserRegistryResponse(boolean isSuccess, String message, int userId) {
        super(isSuccess, message);
        UserId = userId;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }
}
