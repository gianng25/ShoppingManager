package com.example.shoppingmanager.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shoppingmanager.R;

public class HomeActivity extends AppCompatActivity {

    TextView tvWelcome;
    Button btnLogin, btnProducts, btnCart, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tvWelcome = findViewById(R.id.tvWelcome);
        btnLogin = findViewById(R.id.btnLoginScreen);
        btnProducts = findViewById(R.id.btnProducts);
        btnCart = findViewById(R.id.btnCart);
        btnLogout = findViewById(R.id.btnLogout);

        updateStatus();

        btnLogin.setOnClickListener(v ->
                startActivity(new Intent(this, LoginActivity.class)));

        btnProducts.setOnClickListener(v ->
                startActivity(new Intent(this, ProductActivity.class)));

        btnCart.setOnClickListener(v ->
                startActivity(new Intent(this, CartActivity.class)));

        btnLogout.setOnClickListener(v -> {
            SharedPreferences sp = getSharedPreferences("LOGIN", MODE_PRIVATE);
            sp.edit().clear().apply();
            updateStatus();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateStatus();
    }

    private void updateStatus() {
        SharedPreferences sp = getSharedPreferences("LOGIN", MODE_PRIVATE);
        boolean isLogin = sp.getBoolean("isLogin", false);
        String username = sp.getString("username", "");

        if (isLogin) {
            tvWelcome.setText("Xin chào: " + username);
        } else {
            tvWelcome.setText("Bạn chưa đăng nhập");
        }
    }
}