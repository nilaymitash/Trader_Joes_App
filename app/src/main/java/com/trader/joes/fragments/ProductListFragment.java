package com.trader.joes.fragments;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;
import com.google.android.material.badge.ExperimentalBadgeUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseError;
import com.trader.joes.R;
import com.trader.joes.adapter.ProductListAdapter;
import com.trader.joes.model.CartItem;
import com.trader.joes.model.Product;
import com.trader.joes.model.User;
import com.trader.joes.service.AuthService;
import com.trader.joes.service.ProductRetrievalService;
import com.trader.joes.service.UserDataMaintenanceService;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ProductListFragment extends Fragment {

    private SearchView mSearchView;
    private ProgressBar mProgressBar;
    private RecyclerView mProductListView;
    private FloatingActionButton mViewCartBtn;
    private ProductListAdapter mProductAdapter;
    private ProductRetrievalService productRetrievalService;
    private UserDataMaintenanceService userDataMaintenanceService;
    private List<Product> allProducts = new ArrayList<>();
    
    private int totalCartItems = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_product_list, container, false);

        productRetrievalService = new ProductRetrievalService();
        userDataMaintenanceService = new UserDataMaintenanceService();
        mSearchView = view.findViewById(R.id.search_bar);
        mViewCartBtn = view.findViewById(R.id.floating_cart_btn);
        mProgressBar = view.findViewById(R.id.loading_indicator);
        mProductAdapter = new ProductListAdapter(new ArrayList<>(), ProductListFragment.this); //initializing adapter

        animateSlider();
        fetchProductList(view);
        fetchCartInfo();

        mViewCartBtn.setOnClickListener(new ProductListListener());
        mViewCartBtn.getViewTreeObserver().addOnGlobalLayoutListener(new ProductListListener());
        mSearchView.setOnQueryTextListener(new ProductListListener());

        //TODO: Make a decision on showing search option in toolbar
        //setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        inflater.inflate(R.menu.toolbar_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.searchOption);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void fetchCartInfo() {
        Consumer<User> success = new Consumer<User>() {
            @Override
            public void accept(User user) {
                int totalItems = 0;

                for(CartItem item: user.getCartItems()) {
                    totalItems += item.getQty();
                }
                totalCartItems = totalItems;
                if(getActivity() != null) {
                    new ProductListListener().onGlobalLayout();
                }
            }
        };
        
        Consumer<String> failure = new Consumer<String>() {
            @Override
            public void accept(String s) {
                Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
            }
        };
        userDataMaintenanceService.getCurrentUserData(new AuthService().getCurrentUser().getUid(), success, failure);
    }

    private void animateSlider() {
        ObjectAnimator animation = ObjectAnimator.ofInt(mProgressBar, "progress", 0, 100);
        animation.setDuration(3000);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();
    }

    private void fetchProductList(View view) {
        Consumer<List<Product>> productConsumer = new Consumer<List<Product>>() {
            @Override
            public void accept(List<Product> products) {
                allProducts = products;

                mProductAdapter = new ProductListAdapter(allProducts, ProductListFragment.this);
                mProductListView = view.findViewById(R.id.recyclerView);
                mProductListView.setAdapter(mProductAdapter);
                mProgressBar.setVisibility(View.GONE);
            }
        };

        Consumer<DatabaseError> dbErrorConsumer = new Consumer<DatabaseError>() {
            @Override
            public void accept(DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
        productRetrievalService.getAllProducts(productConsumer, dbErrorConsumer);
    }

    private void filter(String text) {
        ArrayList<Product> filteredList = new ArrayList<>();

        for (Product p: allProducts) {
            if(p.getProductName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(p);
            }
        }

        if (filteredList.isEmpty()) {
            Toast.makeText(getActivity(), "No Products Found..", Toast.LENGTH_SHORT).show();
        } else {
            mProductAdapter.filterList(filteredList);
        }
    }

    private class ProductListListener implements SearchView.OnQueryTextListener, ViewTreeObserver.OnGlobalLayoutListener, View.OnClickListener {

        @Override
        public void onClick(View view) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CartFragment()).commit();
        }

        @OptIn(markerClass = ExperimentalBadgeUtils.class)
        @Override
        public void onGlobalLayout() {

            BadgeDrawable badgeDrawable = BadgeDrawable.create(getActivity());
            badgeDrawable.setNumber(totalCartItems);

            //Important to change the position of the Badge
            badgeDrawable.setHorizontalOffset(30);
            badgeDrawable.setVerticalOffset(20);

            BadgeUtils.attachBadgeDrawable(badgeDrawable, mViewCartBtn, null);

            mViewCartBtn.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }

        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            filter(newText);
            return false;
        }
    }
}