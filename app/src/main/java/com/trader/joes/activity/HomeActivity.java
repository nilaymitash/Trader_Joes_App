package com.trader.joes.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

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
import com.trader.joes.service.StorageService;
import com.trader.joes.service.UtilityService;

import java.util.function.Consumer;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, NavigationBarView.OnItemSelectedListener {

    private AuthService authService;
    private RelativeLayout mHomePageLayout;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private NavigationView mNavigationView;
    private BottomNavigationView mBottomNavigationView;
    private ImageView mProfilePic;
    private TextView mUsername;
    private StorageService storageService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        authService = new AuthService();
        storageService = new StorageService();
        //initialize UI components
        mHomePageLayout = findViewById(R.id.home_activity);
        drawerLayout = findViewById(R.id.home_page_drawer_layout);
        mNavigationView = findViewById(R.id.nav_view);
        View hView = mNavigationView.getHeaderView(0);
        mProfilePic = hView.findViewById(R.id.profile_pic);
        mUsername = hView.findViewById(R.id.username_label);
        mBottomNavigationView = findViewById(R.id.bottom_nav_view);

        populateNavHeaderData();

        //set the item selected listener to the navigation view components
        mNavigationView.setNavigationItemSelectedListener(this);
        mBottomNavigationView.setOnItemSelectedListener(this);

        //Initializing ActionBarDrawerToggle which will be responsible for opening and closing the nav view
        mActionBarDrawerToggle = new ActionBarDrawerToggle(
                this, // Activity / Context
                drawerLayout, // Drawer layout
                R.string.navigation_drawer_open, // String to open
                R.string.navigation_drawer_close // String to close
        );

        //Adding the drawer toggle to the home page
        drawerLayout.addDrawerListener(mActionBarDrawerToggle);

        //Syncs the drawer state with home page layout
        mActionBarDrawerToggle.syncState();

        //to always show action bar menu icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new ProductListFragment()).commit();
    }

    private void populateNavHeaderData() {
        populateDisplayName();
        populateProfilePic();
    }

    public void populateDisplayName() {
        FirebaseUser currentUser = authService.getCurrentUser();
        String displayName = currentUser.getDisplayName();
        if(displayName != null && !displayName.trim().equals("")) {
            mUsername.setText(displayName);
        } else {
            mUsername.setText(currentUser.getEmail());
        }
    }

    public void populateDisplayName(String displayName) {
        mUsername.setText(displayName);
    }

    public void populateProfilePic() {
        Consumer<Bitmap> successCallback = new Consumer<Bitmap>() {
            @Override
            public void accept(Bitmap bitmap) {
                populateProfilePic(bitmap);
            }
        };
        storageService.downloadProfilePic(successCallback);
    }

    public void populateProfilePic(Bitmap bitmap) {
        mProfilePic.setImageBitmap(Bitmap.createScaledBitmap(bitmap, mProfilePic.getWidth(), mProfilePic.getHeight(), false));
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
        drawerLayout.closeDrawer(GravityCompat.START);

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

    private void openAccountFragment() {
        UtilityService.hideKeyboard(this, mHomePageLayout);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AccountFragment()).commit();
    }
    
    private void openProductListFragment() {
        UtilityService.hideKeyboard(this, mHomePageLayout);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProductListFragment()).commit();
    }

    private void openBarcodeScanner() {
        UtilityService.hideKeyboard(this, mHomePageLayout);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new BarcodeScannerFragment()).commit();
    }

    private void openFindStoreFragment() {
        UtilityService.hideKeyboard(this, mHomePageLayout);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FindStoreFragment()).commit();
    }

    private void openOrderHistoryFragment() {
        UtilityService.hideKeyboard(this, mHomePageLayout);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new OrderHistoryFragment()).commit();
    }
}