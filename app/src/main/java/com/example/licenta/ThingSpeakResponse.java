package com.example.licenta;

import com.google.gson.annotations.SerializedName;

public class ThingSpeakResponse {
    @SerializedName("created_at")
    private String createdAt;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}


