package com.trader.joes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.trader.joes.R;

/**
 * This main activity presents the user with the following options:
 * Login
 * Create account
 * Continue with Google
 */
public class MainActivity extends AppCompatActivity {

    private Button mLoginBtn;
    private Button mGoogleBtn;
    private Button mSignupBtn;
    private RelativeLayout relativeLayout;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoginBtn = findViewById(R.id.login_btn);
        mGoogleBtn = findViewById(R.id.google_btn);
        mSignupBtn = findViewById(R.id.create_account_btn);
        relativeLayout = findViewById(R.id.main_layout);

        mLoginBtn.setOnClickListener(new MainListener());
        mSignupBtn.setOnClickListener(new MainListener());
        mGoogleBtn.setOnClickListener(new MainListener());

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //if the user is already logged in, navigate to home page
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check condition
        if (requestCode == 100) {
            // When request code is equal to 100 initialize task
            Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            if(signInAccountTask.isSuccessful()) {
                Toast.makeText(this, "Google sign in successful", Toast.LENGTH_SHORT).show();
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = signInAccountTask.getResult(ApiException.class);
                    new MainListener().firebaseAuthWithGoogle(account.getIdToken());
                } catch (ApiException e) {
                    // Google Sign In failed, update UI appropriately
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
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
            // Initialize sign in intent
            Intent intent = mGoogleSignInClient.getSignInIntent();
            // Start activity for result
            startActivityForResult(intent, 100);

        }

        // [START auth_with_google]
        private void firebaseAuthWithGoogle(String idToken) {
            AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
            mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, navigate to home page
                        startActivity(new Intent(MainActivity.this, HomeActivity.class));
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(MainActivity.this, "Google auth failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        // [END auth_with_google]
    }
}