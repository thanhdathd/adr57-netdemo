package com.adr57.netdemo.network.dto;

import com.google.gson.annotations.SerializedName;

public class UploadResponse {
    @SerializedName("message")
    private String message;

    @SerializedName("filename")
    private boolean fileName;

    @SerializedName("url")
    private boolean url;
}
