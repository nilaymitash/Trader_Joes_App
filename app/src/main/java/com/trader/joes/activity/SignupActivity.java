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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.trader.joes.R;

public class SignupActivity extends AppCompatActivity {

    private RelativeLayout mSignupActivity;
    private Button mBackButton;
    private Button mSignupButton;
    private EditText mEmailInput;
    private EditText mPasswordInput;
    private EditText mConfirmPasswordInput;
    private TextView mEmailValidationLabel;
    private TextView mPasswordValidationLabel;
    private boolean isFormValid = false;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mSignupActivity = findViewById(R.id.signup_activity);
        mAuth = FirebaseAuth.getInstance();
        mBackButton = findViewById(R.id.back_to_main_btn);
        mSignupButton = findViewById(R.id.signup_btn);
        mEmailInput = findViewById(R.id.signup_email_input);
        mPasswordInput = findViewById(R.id.signup_password_input);
        mConfirmPasswordInput = findViewById(R.id.confirm_password_input);
        mEmailValidationLabel = findViewById(R.id.email_validation_msg);
        mPasswordValidationLabel = findViewById(R.id.password_validation_msg);

        mEmailInput.addTextChangedListener(new SignupActivityListener(mEmailInput));
        mPasswordInput.addTextChangedListener(new SignupActivityListener(mPasswordInput));
        mConfirmPasswordInput.addTextChangedListener(new SignupActivityListener(mConfirmPasswordInput));

        mBackButton.setOnClickListener(new SignupActivityListener());
        mSignupButton.setOnClickListener(new SignupActivityListener());
    }

    @Override
    protected void onStart() {
        super.onStart();
        //if the user is already logged in, navigate to home page
        //TODO: Navigate the user to home page
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            //navigate to home page
            //add currentUser to intent
            Toast.makeText(this, "User already logged in: " + currentUser.getEmail(), Toast.LENGTH_SHORT).show();
        }
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
                String email = mEmailInput.getText().toString();
                String password = mPasswordInput.getText().toString();
                createAccount(email, password);
                navigateToProductSearch();
            }
        }

        private void navigateToProductSearch() {
            Toast.makeText(SignupActivity.this, "Account created and Login successful", Toast.LENGTH_SHORT).show();
            //startActivity(new Intent(LoginActivity.this, ProductListActivity.class));
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
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                // Sign in success, navigate the user to the home page
                                FirebaseUser user = mAuth.getCurrentUser();
                                //TODO: add user to Intent
                            } else {
                                Snackbar.make(mSignupActivity, "Oops! Something went wrong.", Snackbar.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }
}