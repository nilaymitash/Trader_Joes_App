package com.trader.joes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.trader.joes.R;

public class LoginActivity extends AppCompatActivity {

    private Button mBackButton;
    private TextView mValidationLabel;
    private EditText mUsernameInput;
    private EditText mPasswordInput;
    private Button mLoginButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
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
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            //navigate to home page
            startActivity(new Intent(LoginActivity.this, ProductListActivity.class));
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

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                navigateToProductSearch();
                            } else {
                                mValidationLabel.setVisibility(View.VISIBLE);
                            }
                        }
                    });
        }

        private void navigateToProductSearch() {
        Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, ProductListActivity.class));
        }

        private void navigateBack() {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
    }
}