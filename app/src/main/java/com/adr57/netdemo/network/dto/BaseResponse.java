package com.adr57.netdemo.network.dto;

import com.adr57.netdemo.model.Product;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BaseResponse<T> {
    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private T data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public List<Product> getDataForProduct() {
        return (List<Product>) data;
    }


    public void setData(T data) {
        this.data = data;
    }
}
