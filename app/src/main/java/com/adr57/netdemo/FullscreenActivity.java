package com.adr57.netdemo;

import android.annotation.SuppressLint;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.adr57.netdemo.databinding.ActivityFullscreenBinding;
import com.adr57.netdemo.storage.PreferencesRepository;
import com.bumptech.glide.Glide;

import io.reactivex.rxjava3.core.Flowable;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {
    ImageView imageView;
    TextView tvName, tvEmail;
    Button btnGoBack;
    PreferencesRepository preferencesRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_fullscreen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupViews();
        setupListeners();
        setupData();
    }

    private void setupData() {
        Intent intent = getIntent();
        String avatar = intent.getStringExtra("avatar");
        String name = intent.getStringExtra("name");
        String email = intent.getStringExtra("email");

        tvName.setText(name);
        tvEmail.setText(email);
        Glide.with(this)
                .load(avatar)
                .placeholder(R.drawable.ic_placeholder)
                .into(imageView);

        MyApplication application = (MyApplication) getApplication();
        preferencesRepository = new PreferencesRepository(application.getDataStore());

        Flowable<String> usernameFlow  = preferencesRepository.getUsername();
        usernameFlow.subscribe(value -> {
           Log.i("pref: data", "username: "+value);
        });
    }

    private void setupListeners() {
        btnGoBack.setOnClickListener(v -> {
            finish();
        });
    }

    private void setupViews() {
        imageView = findViewById(R.id.avatar);
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        btnGoBack = findViewById(R.id.btnGoBack);
    }

}