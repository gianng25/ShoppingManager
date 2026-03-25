package com.example.shoppingmanager.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shoppingmanager.R;
import com.example.shoppingmanager.dal.AppDB;
import com.example.shoppingmanager.entities.Account;

public class LoginActivity extends AppCompatActivity {

    EditText edtUser, edtPass;
    Button btnLogin;
    AppDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUser = findViewById(R.id.edtUser);
        edtPass = findViewById(R.id.edtPass);
        btnLogin = findViewById(R.id.btnLogin);

        db = AppDB.getInstance(this);

        btnLogin.setOnClickListener(v -> {
            String u = edtUser.getText().toString().trim();
            String p = edtPass.getText().toString().trim();

            Account acc = db.dao().login(u, p);

            if (acc != null) {
                SharedPreferences sp = getSharedPreferences("LOGIN", MODE_PRIVATE);
                sp.edit()
                        .putString("username", u)
                        .putBoolean("isLogin", true)
                        .apply();

                int pendingProductId = sp.getInt("pendingProductId", -1);

                Toast.makeText(this, "Login thành công", Toast.LENGTH_SHORT).show();

                if (pendingProductId != -1) {
                    startActivity(new Intent(this, ProductActivity.class));
                } else {
                    startActivity(new Intent(this, HomeActivity.class));
                }

                finish();
            } else {
                Toast.makeText(this, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
            }
        });
    }
}