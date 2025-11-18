package com.adr57.netdemo.network;

import com.adr57.netdemo.model.Product;
import com.adr57.netdemo.model.User;
import com.adr57.netdemo.network.dto.LoginRequest;
import com.adr57.netdemo.network.dto.LoginResponse;
import com.adr57.netdemo.network.dto.ProductListResponse;
import com.adr57.netdemo.network.dto.RefreshRequest;
import com.adr57.netdemo.network.dto.TokenResponse;
import com.adr57.netdemo.network.dto.UploadResponse;
import com.adr57.netdemo.network.dto.UserListResponse;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {

    // GET requests
    @GET("users")
    Call<UserListResponse> getUsers();

    @GET("products")
    Call<ProductListResponse> getProducts();

    @GET("users/{id}")
    Call<User> getUser(@Path("id") int userId);

    @GET("products/{id}")
    Call<Product> getProduct(@Path("id") int productId);

    // POST requests
    @POST("users")
    Call<User> createUser(@Body User user);

    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    // PUT requests
    @PUT("users/{id}")
    Call<User> updateUser(@Path("id") int userId, @Body User user);

    // DELETE requests
    @DELETE("users/{id}")
    Call<Void> deleteUser(@Path("id") int userId);

    // Query parameters
    @GET("users")
    Call<UserListResponse> getUsersByPage(@Query("page") int page, @Query("limit") int limit);

    // Form URL encoded
    @POST("refresh")
    Call<TokenResponse> refreshToken(@Body RefreshRequest refreshToken);

    // Multipart for file upload
    @Multipart
    @POST("upload")
    Call<UploadResponse> uploadFile(@Part MultipartBody.Part file);

    @Multipart
    @POST("users/{id}/avatar")
    Call<User> uploadAvatar(@Path("id") int userId, @Part MultipartBody.Part avatar);

    @GET("users/search")
    Call<List<User>> searchUsers(@Query("q") String query);
}
