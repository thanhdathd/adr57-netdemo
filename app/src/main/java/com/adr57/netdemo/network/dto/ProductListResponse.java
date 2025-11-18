package com.adr57.netdemo.network.dto;

import com.adr57.netdemo.model.Product;
import com.google.gson.annotations.SerializedName;

public class ProductListResponse extends BaseResponse<Product>{

    @SerializedName("pagination")
    private Pagination pagination;

    // Getters and setters
}
