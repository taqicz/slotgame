package com.example.javaminggu10_1.model;

import com.google.gson.annotations.SerializedName;

public class AddMahasiswaResponse{

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private boolean status;

    public String getMessage(){
        return message;
    }

    public boolean isStatus(){
        return status;
    }
}
