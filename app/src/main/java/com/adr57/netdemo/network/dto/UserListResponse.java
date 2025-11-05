package com.adr57.netdemo.network.dto;

import com.adr57.netdemo.model.User;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserListResponse extends BaseResponse<List<User>> {
    @SerializedName("pagination")
    private Pagination pagination;

    // Getters and setters
}
