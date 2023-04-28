package com.trader.joes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.trader.joes.R;
import com.trader.joes.fragments.AccountFragment;
import com.trader.joes.fragments.BarcodeScannerFragment;
import com.trader.joes.fragments.FindStoreFragment;
import com.trader.joes.fragments.OrderHistoryFragment;
import com.trader.joes.fragments.ProductListFragment;
import com.trader.joes.service.AuthService;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, NavigationBarView.OnItemSelectedListener {

    private AuthService authService;
    private DrawerLayout mHomePageLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private NavigationView mNavigationView;
    private BottomNavigationView mBottomNavigationView;
    private ImageView mProfilePic;
    private TextView mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        authService = new AuthService();

        mHomePageLayout = findViewById(R.id.home_page_drawer_layout);
        mNavigationView = findViewById(R.id.nav_view);
        mBottomNavigationView = findViewById(R.id.bottom_nav_view);

        populateNavHeaderData();

        //set the item selected listener to the navigation view components
        mNavigationView.setNavigationItemSelectedListener(this);
        mBottomNavigationView.setOnItemSelectedListener(this);

        //Initializing ActionBarDrawerToggle which will be responsible for opening and closing the nav view
        mActionBarDrawerToggle = new ActionBarDrawerToggle(
                this, // Activity / Context
                mHomePageLayout, // Drawer layout
                R.string.navigation_drawer_open, // String to open
                R.string.navigation_drawer_close // String to close
        );

        //Adding the drawer toggle to the home page
        mHomePageLayout.addDrawerListener(mActionBarDrawerToggle);

        //Syncs the drawer state with home page layout
        mActionBarDrawerToggle.syncState();

        //to always show action bar menu icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new ProductListFragment()).commit();
    }

    private void populateNavHeaderData() {
        FirebaseUser currentUser = authService.getCurrentUser();
        View hView = mNavigationView.getHeaderView(0);
        mProfilePic = hView.findViewById(R.id.profile_pic);
        mUsername = hView.findViewById(R.id.username_label);

        String displayName = currentUser.getDisplayName();
        if(displayName != null && !displayName.trim().equals("")) {
            mUsername.setText(displayName);
        } else {
            mUsername.setText(currentUser.getEmail());
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
            case R.id.nav_view_products:
            case R.id.bottom_home_nav:
                openProductListFragment();
                break;
            case R.id.nav_find_a_store: openFindStoreFragment();
                break;
            case R.id.nav_account:
            case R.id.bottom_account_nav:
                openAccountFragment();
                break;
            case R.id.nav_order_history: openOrderHistoryFragment();
                break;
            case R.id.nav_logout: authService.signOut(); signOutNavigation();
                break;
            case R.id.bottom_barcode_nav: openBarcodeScanner();
                break;
            default: break;
        }

        /** Close the navigation drawer once a nav item is clicked */
        mHomePageLayout.closeDrawer(GravityCompat.START);

        return true;
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

    private void displayComingSoonMsg() {
        Toast.makeText(this, "Coming soon...", Toast.LENGTH_SHORT).show();
    }
    
    private void openAccountFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AccountFragment()).commit();
    }
    
    private void openProductListFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProductListFragment()).commit();
    }

    private void openBarcodeScanner() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new BarcodeScannerFragment()).commit();
    }

    private void openFindStoreFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FindStoreFragment()).commit();
    }

    private void openOrderHistoryFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new OrderHistoryFragment()).commit();
    }
}