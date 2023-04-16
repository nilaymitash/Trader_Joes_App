package com.trader.joes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.trader.joes.R;
import com.trader.joes.fragments.ProductListFragment;
import com.trader.joes.model.Product;
import com.trader.joes.service.AuthService;
import com.trader.joes.service.ProductRetrievalService;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AuthService authService;
    private ProductRetrievalService productRetrievalService;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private NavigationView mNavigationView;
    private List<Product> allProducts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        authService = new AuthService();

        mDrawerLayout = findViewById(R.id.nav_drawer_layout);
        mNavigationView = findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(
                this, // Activity / Context
                mDrawerLayout, // DrawerLayout
                R.string.navigation_drawer_open, // String to open
                R.string.navigation_drawer_close // String to close
        );
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //to always show action bar menu icon
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new ProductListFragment()).commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = authService.getCurrentUser();
        if(currentUser != null){
            //this.mWelcomeLabel.setText("Welcome " + currentUser.getEmail());
        } else {
            //If the user's session has timed out while on this page, send them back to main screen
            signOutNavigation();
        }
    }

    private void signOutNavigation() {
        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        intent.putExtra(getResources().getString(R.string.signing_out_extra), true);
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_view_products: displayComingSoonMsg();
                break;
            case R.id.nav_find_a_store: displayComingSoonMsg();
                break;
            case R.id.nav_account: displayComingSoonMsg();
                break;
            case R.id.nav_order_history: displayComingSoonMsg();
                break;
            case R.id.nav_logout: authService.signOut(); signOutNavigation();
                break;
            default: break;
        }

        /** Close the navigation drawer */
        mDrawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }
    
    private void displayComingSoonMsg() {
        Toast.makeText(this, "Coming soon...", Toast.LENGTH_SHORT).show();
    }

    /**
     * to toggle drawer using action bar menu icon
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (mActionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}