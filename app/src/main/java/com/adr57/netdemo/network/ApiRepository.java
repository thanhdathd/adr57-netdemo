package com.adr57.netdemo.network;

import androidx.annotation.NonNull;

import com.adr57.netdemo.model.Product;
import com.adr57.netdemo.model.User;
import com.adr57.netdemo.network.dto.LoginRequest;
import com.adr57.netdemo.network.dto.LoginResponse;
import com.adr57.netdemo.network.dto.ProductListResponse;
import com.adr57.netdemo.network.dto.UserListResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiRepository {
    private ApiService apiService;

    public ApiRepository() {
        apiService = ApiClient.getApi();
    }

    // GET request với callback
    public void getUsers(final ApiCallback<List<User>> callback) {
        Call<UserListResponse> call = apiService.getUsers();
        call.enqueue(new Callback<UserListResponse>() {
            @Override
            public void onResponse(Call<UserListResponse> call, Response<UserListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isSuccess()) {
                        callback.onSuccess(response.body().getData());
                    } else {
                        callback.onError(response.body().getMessage());
                    }
                } else {
                    callback.onError("Failed to get users");
                }
            }

            @Override
            public void onFailure(Call<UserListResponse> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void getProducts(final ApiCallback<List<Product>> callback){
        Call<ProductListResponse> call = apiService.getProducts();
        call.enqueue(new Callback<ProductListResponse>() {
            @Override
            public void onResponse(@NonNull Call<ProductListResponse> call, @NonNull Response<ProductListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isSuccess()) {
                        callback.onSuccess(response.body().getDataForProduct());
                    } else {
                        callback.onError(response.body().getMessage());
                    }
                } else {
                    callback.onError("Failed to get products");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProductListResponse> call, @NonNull Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    // POST request
    public void createUser(User user, final ApiCallback<User> callback) {
        Call<User> call = apiService.createUser(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Failed to create user");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void login(LoginRequest loginRequest, ApiCallback<LoginResponse> apiCallback) {
        Call<LoginResponse> call = apiService.login(loginRequest);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    apiCallback.onSuccess(response.body());
                } else {
                    apiCallback.onError("Login failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                apiCallback.onError("Network error: " + t.getMessage());
            }
        });
    }
}
