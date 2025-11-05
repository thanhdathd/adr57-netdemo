package com.adr57.netdemo.network;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import com.adr57.netdemo.LoginActivity;
import com.adr57.netdemo.auth.SharedPrefManager;
import com.adr57.netdemo.network.dto.TokenResponse;
import com.google.gson.Gson;

import okhttp3.*;
import java.io.IOException;

public class AuthInterceptor implements Interceptor {
    private final TokenRepository tokenRepo;

    public AuthInterceptor(TokenRepository tokenRepo) {
        this.tokenRepo = tokenRepo;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        String token = tokenRepo.getAccessToken();
        if (token != null) {
            request = request.newBuilder()
                    .header("Authorization", "Bearer " + token)
                    .build();
        }

        Response response = chain.proceed(request);
        if (response.code() == 401 || response.code() == 403) {
            response.close();

            if (tokenRepo.refreshToken()) { // auto gain new access token
                // refresh ok → retry
                String newToken = tokenRepo.getAccessToken();
                Request retry = request.newBuilder()
                        .header("Authorization", "Bearer " + newToken)
                        .build();
                return chain.proceed(retry);
            } else {
                tokenRepo.forceLogout();
            }
        }

        return response;
    }
}

