package com.adr57.netdemo.network;

import android.content.Context;

import com.adr57.netdemo.auth.SharedPrefManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ApiClient {
    protected static final String BASE_URL = "http://10.0.2.2:3000/";
//    protected static final String BASE_URL = "https://jsonplaceholder.typicode.com/";
    private static Retrofit retrofit = null;
    private static Context appContext;
    private static TokenRepository tokenRepository;

    // Khởi tạo với Context
    public static void initialize(Context context) {
        appContext = context.getApplicationContext();
    }

    public static Retrofit getClient() {
        if (retrofit == null) {
            // Tạo logging interceptor
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            // Tạo OkHttp client
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(logging);
            httpClient.connectTimeout(30, TimeUnit.SECONDS);
            httpClient.readTimeout(30, TimeUnit.SECONDS);
            httpClient.writeTimeout(30, TimeUnit.SECONDS);

            // Thêm auth interceptor nếu cần
            tokenRepository = new TokenRepository(appContext);
            httpClient.addInterceptor(new AuthInterceptor(tokenRepository));

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
        }
        return retrofit;
    }

    public static ApiService getApi() {
        ApiService client = getClient().create(ApiService.class);
        if (tokenRepository != null) {
            tokenRepository.setApiService(client);
        }
        return client;
    }

    // Auth Interceptor cho việc thêm token vào header
//    private static class AuthInterceptor implements Interceptor {
//        @Override
//        public Response intercept(Chain chain) throws IOException {
//            Request original = chain.request();
//
//            // Thêm token nếu có
//            String token = SharedPrefManager.getInstance(appContext).getToken();
//            if (token != null && !token.isEmpty()) {
//                Request.Builder builder = original.newBuilder()
//                        .header("Authorization", "Bearer " + token);
//                original = builder.build();
//            }
//
//            return chain.proceed(original);
//        }
//    }
}
