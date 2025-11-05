package com.adr57.netdemo;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.adr57.netdemo.auth.AuthManager;
import com.adr57.netdemo.auth.SharedPrefManager;
import com.adr57.netdemo.model.User;
import com.adr57.netdemo.network.ApiCallback;
import com.adr57.netdemo.network.ApiRepository;
import com.adr57.netdemo.network.dto.LoginRequest;
import com.adr57.netdemo.network.dto.LoginResponse;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;
import java.util.Objects;

import eightbitlab.com.blurview.BlurTarget;
import eightbitlab.com.blurview.BlurView;

public class LoginActivity extends AppCompatActivity {

    Button btnSignIn;

    TextInputEditText edtEmail, etPassword;
    TextView errorTv;
    private ApiRepository apiRepository;
    private SharedPrefManager sharedPrefManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        // Check nếu đã login thì chuyển đến MainActivity
        sharedPrefManager = SharedPrefManager.getInstance(this);
        if (sharedPrefManager.isLoggedIn()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }
        setEdgePadding();
        setupBlurView();
        setupViews();
        apiRepository = new ApiRepository();
    }

    private void setupViews() {
        edtEmail = findViewById(R.id.edtEmail);
        etPassword = findViewById(R.id.etPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        errorTv = findViewById(R.id.errorTv);

        btnSignIn.setOnClickListener(v -> login());
    }

    private void login() {
        String email = Objects.requireNonNull(edtEmail.getText()).toString();
        String password = Objects.requireNonNull(etPassword.getText()).toString();
        Log.i("LOGIN", "Email: " + email);
        Log.i("LOGIN", "Pass: " + password);

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        showLoading();

        LoginRequest loginRequest = new LoginRequest(email, password);
        apiRepository.login(loginRequest, new ApiCallback<LoginResponse>() {
            @Override
            public void onSuccess(LoginResponse loginResponse) {
                hideLoading();

                // Lưu thông tin user
                sharedPrefManager.saveUserData(
                        loginResponse.getToken(),
                        loginResponse.getRefreshToken(),
                        loginResponse.getUserId(),
                        loginResponse.getUsername(),
                        loginResponse.getEmail()
                );

                // Chuyển đến MainActivity
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onError(String error) {
                hideLoading();
                Toast.makeText(LoginActivity.this, "Login failed: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showLoading() {
        Log.i("LOGIN", "Show loading");
        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        Log.i("LOGIN", "Hide loading");
        findViewById(R.id.progressBar).setVisibility(View.GONE);
    }


    private void setEdgePadding() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, 0, systemBars.right, 0);
            return insets;
        });
    }

    private void setupBlurView() {
        float radius = 10f;
        BlurView blurContainer = findViewById(R.id.blurContainer);
        View decorView = getWindow().getDecorView();
        BlurTarget target = findViewById(R.id.target);

        Drawable windowBackground = decorView.getBackground();

        blurContainer.setupWith(target)
                .setFrameClearDrawable(windowBackground) // Optional. Useful when your root has a lot of transparent background, which results in semi-transparent blurred content. This will make the background opaque
                .setBlurRadius(radius);
        blurContainer.setOutlineProvider(ViewOutlineProvider.BACKGROUND);
        blurContainer.setClipToOutline(true);
    }
}