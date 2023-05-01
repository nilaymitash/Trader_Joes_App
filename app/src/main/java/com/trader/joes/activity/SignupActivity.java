package com.trader.joes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.trader.joes.R;
import com.trader.joes.service.AuthService;

import java.util.function.Consumer;

public class SignupActivity extends AppCompatActivity {

    private Button mBackButton;
    private Button mSignupButton;
    private TextView mValidationLabel;
    private EditText mEmailInput;
    private EditText mPasswordInput;
    private EditText mConfirmPasswordInput;
    private TextView mEmailValidationLabel;
    private TextView mPasswordValidationLabel;
    private boolean isFormValid = false;
    private AuthService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        authService = new AuthService();
        mBackButton = findViewById(R.id.back_to_main_btn);
        mSignupButton = findViewById(R.id.signup_btn);
        mEmailInput = findViewById(R.id.signup_email_input);
        mPasswordInput = findViewById(R.id.signup_password_input);
        mConfirmPasswordInput = findViewById(R.id.confirm_password_input);
        mEmailValidationLabel = findViewById(R.id.email_validation_msg);
        mPasswordValidationLabel = findViewById(R.id.password_validation_msg);
        mValidationLabel = findViewById(R.id.validation_label);

        /**
         * TextChangedListener is used here to validate
         * the email and password input in real time
         * as the user types the information
         */
        mEmailInput.addTextChangedListener(new SignupActivityListener(mEmailInput));
        mPasswordInput.addTextChangedListener(new SignupActivityListener(mPasswordInput));
        mConfirmPasswordInput.addTextChangedListener(new SignupActivityListener(mConfirmPasswordInput));

        mBackButton.setOnClickListener(new SignupActivityListener());
        mSignupButton.setOnClickListener(new SignupActivityListener());
    }

    class SignupActivityListener implements View.OnClickListener, TextWatcher {

        private final EditText mEditText;

        public SignupActivityListener() {
            //dummy input to bypass possible null pointer exception
            this.mEditText = new EditText(SignupActivity.this);
        }

        public SignupActivityListener(EditText mEditText) {
           this.mEditText = mEditText;
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.back_to_main_btn: navigateBack(); break;
                case R.id.signup_btn: signup(); break;
                default: break;
            }
        }

        private void signup() {
            if(isFormValid) {
                mValidationLabel.setVisibility(View.GONE);
                String email = mEmailInput.getText().toString();
                String password = mPasswordInput.getText().toString();
                createAccount(email, password);
            } else {
                mValidationLabel.setVisibility(View.VISIBLE);
            }
        }

        private void navigateBack() {
            startActivity(new Intent(SignupActivity.this, MainActivity.class));
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            //do nothing
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            //do nothing
        }

        /**
         * This TextWatcher method is used to validate user input for email and password.
         * This method gets called everytime the text changes in the input
         * @param editable
         */
        @Override
        public void afterTextChanged(Editable editable) {
            switch (mEditText.getId()) {
                case R.id.signup_email_input: validateEmail(); break;
                case R.id.signup_password_input:
                case R.id.confirm_password_input:
                    validatePassword(); break;
                default: break;
            }

        }

        private void validateEmail() {
            String email = mEmailInput.getText().toString();
            if(TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                mEmailValidationLabel.setVisibility(View.VISIBLE);
                isFormValid = false;
            } else {
                mEmailValidationLabel.setVisibility(View.GONE);
                isFormValid = true;
            }
        }

        private void validatePassword() {
            String password = mPasswordInput.getText().toString();
            String confirmPassword = mConfirmPasswordInput.getText().toString();

            if(!password.equals(confirmPassword)) {
                mPasswordValidationLabel.setVisibility(View.VISIBLE);
                isFormValid = false;
            } else {
                mPasswordValidationLabel.setVisibility(View.GONE);
                isFormValid = true;
            }
        }

        private void createAccount(String email, String password) {
            /**
             * This callback function is passed in to be executed when
             * account creation is successful
             */
            Consumer<Void> signupSuccessConsumer = new Consumer<Void>() {
                @Override
                public void accept(Void unused) {
                    startActivity(new Intent(SignupActivity.this, HomeActivity.class));
                }
            };

            /**
             * This callback function is passed in to be executed when
             * account creation fails.
             */
            Consumer<String> signupFailureConsumer = new Consumer<String>() {
                @Override
                public void accept(String s) {
                    Toast.makeText(SignupActivity.this, s, Toast.LENGTH_LONG).show();
                }
            };
            authService.createAccount(email, password, SignupActivity.this, signupSuccessConsumer, signupFailureConsumer);
        }
    }
}