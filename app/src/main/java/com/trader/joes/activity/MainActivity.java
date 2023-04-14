package com.trader.joes.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.trader.joes.R;

public class MainActivity extends AppCompatActivity {

    private Button mLoginBtn;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoginBtn = findViewById(R.id.login_btn);
        relativeLayout = findViewById(R.id.main_layout);
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean signingOut = getIntent().getBooleanExtra(getResources().getString(R.string.signing_out_extra), false);

        if (signingOut) {
            Snackbar.make(relativeLayout, getResources().getString(R.string.sign_out_msg), Snackbar.LENGTH_LONG).show();
        }
    }
}