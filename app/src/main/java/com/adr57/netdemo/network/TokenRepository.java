package com.adr57.netdemo.network;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.adr57.netdemo.auth.AuthManager;
import com.adr57.netdemo.auth.SharedPrefManager;
import com.adr57.netdemo.network.ApiClient;
import com.adr57.netdemo.network.ApiService;
import com.adr57.netdemo.LoginActivity;
import com.adr57.netdemo.network.dto.RefreshRequest;
import com.adr57.netdemo.network.dto.TokenResponse;

import retrofit2.Call;
import retrofit2.Response;

public class TokenRepository {

    private final SharedPrefManager sharedPrefs;
    private volatile AuthManager authManager;

    private ApiService apiService;
    private final Context context;

    public TokenRepository(Context context) {
        this.context = context;
        this.sharedPrefs = SharedPrefManager.getInstance(context);
        this.authManager = AuthManager.getInstance(context);
//        this.apiService = ApiClient.getClient().create(ApiService.class);
    }

    public void setApiService(ApiService apiService) {
        this.apiService = apiService;
    }

    /**
     * Lấy access token hiện tại
     */
    public String getAccessToken() {
        return sharedPrefs.getToken();
    }

    /**
     * Lấy refresh token hiện tại
     */
    public String getRefreshToken() {
        return sharedPrefs.getRefreshToken();
    }

    /**
     * Thực hiện refresh token đồng bộ (gọi khi 401)
     * @return true nếu refresh thành công và token mới đã được lưu
     */
    public synchronized boolean refreshToken() {
        String refreshToken = getRefreshToken();
        if (refreshToken == null || refreshToken.isEmpty()) {
            forceLogout();
            return false;
        }

        try {
            RefreshRequest request = new RefreshRequest(refreshToken);
            Call<TokenResponse> call = apiService.refreshToken(request);
            Response<TokenResponse> response = call.execute();

            if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                TokenResponse body = response.body();
                sharedPrefs.updateTokens(body.getAccessToken(), body.getRefreshToken());
                return true;
            } else {
                forceLogout();
            }
        } catch (Exception e) {
            e.printStackTrace();
            forceLogout();
        }

        return false;
    }

    /**
     * Khi refresh thất bại → logout user
     */
    public void forceLogout() {
        Log.d("TokenRepository", "Force logout");
        sharedPrefs.clear();
        authManager.logout();

        new Handler(Looper.getMainLooper()).post(() -> {
            Intent intent = new Intent(context, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
        });
    }
}

