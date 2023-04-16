package com.trader.joes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.trader.joes.R;
import com.trader.joes.service.AuthService;

import java.util.function.Consumer;

public class LoginActivity extends AppCompatActivity {

    private Button mBackButton;
    private TextView mValidationLabel;
    private EditText mUsernameInput;
    private EditText mPasswordInput;
    private Button mLoginButton;
    private AuthService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        authService = new AuthService();
        mBackButton = findViewById(R.id.back_to_main_btn);
        mValidationLabel = findViewById(R.id.validation_label);
        mUsernameInput = findViewById(R.id.username_input);
        mPasswordInput = findViewById(R.id.password_input);
        mLoginButton = findViewById(R.id.login_btn);

        mBackButton.setOnClickListener(new LoginActivityListener());
        mLoginButton.setOnClickListener(new LoginActivityListener());
    }

    @Override
    protected void onStart() {
        super.onStart();
        //if the user is already logged in, navigate to home page
        if(authService.isUserLoggedIn()) {
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        }
    }

    class LoginActivityListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.back_to_main_btn: navigateBack(); break;
                case R.id.login_btn: login(); break;
                default: break;
            }
        }

        private void login() {
            String email = mUsernameInput.getText().toString();
            String password = mPasswordInput.getText().toString();
            authService.signIn(email, password, LoginActivity.this, navigateToProductSearch, (Object obj) -> {mValidationLabel.setVisibility(View.VISIBLE);});
        }

        Consumer navigateToProductSearch = (Object object) -> {
            Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        };

        private void navigateBack() {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
    }
}