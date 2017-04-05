package com.example.rodrigo.trukertrukersoft.service;

import com.example.rodrigo.trukertrukersoft.service.request.UserRegistryRequest;
import com.example.rodrigo.trukertrukersoft.service.request.LoginRequest;
import com.example.rodrigo.trukertrukersoft.service.response.UserRegistryResponse;
import com.example.rodrigo.trukertrukersoft.service.response.LoginResponse;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Headers;
import retrofit.http.POST;

/**
 * Created by Rodrigo on 28/03/2017.
 */

public interface ITruckerSoftService {
    @Headers("Content-Type: application/json")
    @POST("/User")
    void UserRegistry(@Body UserRegistryRequest userRegistryRequest, Callback<UserRegistryResponse> callback);

    @Headers("Content-Type: application/json")
    @POST("/Session")
    void Login(@Body LoginRequest loginRequest, Callback<LoginResponse> callback);
}
