package com.trader.joes.fragments;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseError;
import com.trader.joes.R;
import com.trader.joes.adapter.ProductListAdapter;
import com.trader.joes.model.Product;
import com.trader.joes.service.ProductRetrievalService;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ProductListFragment extends Fragment {

    private SearchView mSearchView;
    private ProgressBar mProgressBar;
    private RecyclerView mProductListView;
    //private Button mViewCartBtn;
    private FloatingActionButton mViewCartBtn;
    private ProductListAdapter mProductAdapter;
    private ProductRetrievalService productRetrievalService;
    private List<Product> allProducts = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_product_list, container, false);

        productRetrievalService = new ProductRetrievalService();
        mSearchView = view.findViewById(R.id.search_bar);
        mViewCartBtn = view.findViewById(R.id.floating_cart_btn);
        mProgressBar = view.findViewById(R.id.loading_indicator);
        mProductAdapter = new ProductListAdapter(new ArrayList<>(), ProductListFragment.this); //initializing adapter

        ObjectAnimator animation = ObjectAnimator.ofInt(mProgressBar, "progress", 0, 100);
        animation.setDuration(3000);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();

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

        mViewCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CartFragment()).commit();
            }
        });

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

        return view;
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
}