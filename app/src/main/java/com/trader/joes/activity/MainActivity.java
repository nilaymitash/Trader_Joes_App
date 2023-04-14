package com.trader.joes.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.trader.joes.R;

public class MainActivity extends AppCompatActivity {

    private Button mLoginBtn;
    private Button mGoogleBtn;
    private Button mSignupBtn;
    private RelativeLayout relativeLayout;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mLoginBtn = findViewById(R.id.login_btn);
        mGoogleBtn = findViewById(R.id.google_btn);
        mSignupBtn = findViewById(R.id.create_account_btn);
        relativeLayout = findViewById(R.id.main_layout);

        mLoginBtn.setOnClickListener(new MainListener());
        mGoogleBtn.setOnClickListener(new MainListener());
        mSignupBtn.setOnClickListener(new MainListener());
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

    @Override
    protected void onResume() {
        super.onResume();
        boolean signingOut = getIntent().getBooleanExtra(getResources().getString(R.string.signing_out_extra), false);

        if (signingOut) {
            Snackbar.make(relativeLayout, getResources().getString(R.string.sign_out_msg), Snackbar.LENGTH_LONG).show();
        }
    }

    class MainListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.login_btn: loadLoginScreen(); break;
                case R.id.create_account_btn: loadSignupScreen(); break;
                case R.id.google_btn: continueWithGoogle(); break;
                default: break;
            }
        }

        public void loadLoginScreen() {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }

        public void loadSignupScreen() {
            startActivity(new Intent(MainActivity.this, SignupActivity.class));
        }

        public void continueWithGoogle() {
            Toast.makeText(MainActivity.this, "Google integration coming soon", Toast.LENGTH_SHORT).show();
        }
    }
}