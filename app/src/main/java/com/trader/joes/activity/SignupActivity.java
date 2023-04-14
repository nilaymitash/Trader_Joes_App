package com.trader.joes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.trader.joes.R;

public class SignupActivity extends AppCompatActivity {

    private Button mBackButton;
    private Button mSignupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mBackButton = findViewById(R.id.back_to_main_btn);
        mSignupButton = findViewById(R.id.signup_btn);

        mBackButton.setOnClickListener(new SignupActivityListener());
        mSignupButton.setOnClickListener(new SignupActivityListener());
    }

    class SignupActivityListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.back_to_main_btn: navigateBack(); break;
                case R.id.signup_btn: signup(); break;
                default: break;
            }
        }

        private void signup() {
            /*String username = mUsernameInput.getText().toString();
            String password = mPasswordInput.getText().toString();

            if(username.equals("admin") && password.equals("admin")) {
                mValidationLabel.setVisibility(View.GONE);
                navigateToProductSearch();
            } else {
                mValidationLabel.setVisibility(View.VISIBLE);
            }*/
            navigateToProductSearch();
        }

        private void navigateToProductSearch() {
            Toast.makeText(SignupActivity.this, "Account created and Login successful", Toast.LENGTH_SHORT).show();
            //startActivity(new Intent(LoginActivity.this, ProductListActivity.class));
        }

        private void navigateBack() {
            startActivity(new Intent(SignupActivity.this, MainActivity.class));
        }
    }
}