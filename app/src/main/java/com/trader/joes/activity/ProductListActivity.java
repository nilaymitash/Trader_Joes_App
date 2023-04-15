package com.trader.joes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.trader.joes.R;

public class ProductListActivity extends AppCompatActivity {

    private TextView mWelcomeLabel;
    private Button mLogoutBtn;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        mWelcomeLabel = findViewById(R.id.welcome_msg);
        mLogoutBtn = findViewById(R.id.logout_btn);
        mAuth = FirebaseAuth.getInstance();

        mLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                signOutNavigation();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            this.currentUser = currentUser;
            this.mWelcomeLabel.setText("Welcome " + currentUser.getEmail());
        } else {
            //If the user's session has timed out while on this page, send them back to main screen
            signOutNavigation();
        }
    }

    private void signOutNavigation() {
        Intent intent = new Intent(ProductListActivity.this, MainActivity.class);
        intent.putExtra(getResources().getString(R.string.signing_out_extra), true);
        startActivity(intent);
    }
}