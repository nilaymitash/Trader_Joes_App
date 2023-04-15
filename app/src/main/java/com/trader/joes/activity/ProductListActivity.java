package com.trader.joes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.trader.joes.R;
import com.trader.joes.model.Product;
import com.trader.joes.service.AuthService;
import com.trader.joes.service.ProductRetrievalService;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ProductListActivity extends AppCompatActivity {

    private TextView mWelcomeLabel;
    private Button mLogoutBtn;
    private AuthService authService;
    private ProductRetrievalService productRetrievalService;
    private List<Product> allProducts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        authService = new AuthService();
        productRetrievalService = new ProductRetrievalService();
        mWelcomeLabel = findViewById(R.id.welcome_msg);
        mLogoutBtn = findViewById(R.id.logout_btn);

        Consumer<List<Product>> productConsumer = new Consumer<List<Product>>() {
            @Override
            public void accept(List<Product> products) {
                allProducts = products;
                //populate recycler view list
                //System.out.println(allProducts);
            }
        };

        Consumer<DatabaseError> dbErrorConsumer = new Consumer<DatabaseError>() {
            @Override
            public void accept(DatabaseError databaseError) {
                Toast.makeText(ProductListActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
        productRetrievalService.getAllProducts(productConsumer, dbErrorConsumer);



        mLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authService.signOut();
                signOutNavigation();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = authService.getCurrentUser();
        if(currentUser != null){
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