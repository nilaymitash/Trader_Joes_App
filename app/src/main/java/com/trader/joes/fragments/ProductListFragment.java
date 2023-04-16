package com.trader.joes.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.trader.joes.R;
import com.trader.joes.activity.HomeActivity;
import com.trader.joes.adapter.ProductListAdapter;
import com.trader.joes.model.Product;
import com.trader.joes.service.ProductRetrievalService;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ProductListFragment extends Fragment {

    private ProductListAdapter mProductAdapter;
    private RecyclerView mProductListView;
    private ProductRetrievalService productRetrievalService;
    private List<Product> allProducts = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        productRetrievalService = new ProductRetrievalService();
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);

        Consumer<List<Product>> productConsumer = new Consumer<List<Product>>() {
            @Override
            public void accept(List<Product> products) {
                allProducts = products;

                mProductAdapter = new ProductListAdapter(allProducts);
                mProductListView = view.findViewById(R.id.recyclerView);
                mProductListView.setAdapter(mProductAdapter);
            }
        };

        Consumer<DatabaseError> dbErrorConsumer = new Consumer<DatabaseError>() {
            @Override
            public void accept(DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
        productRetrievalService.getAllProducts(productConsumer, dbErrorConsumer);

        return view;
    }
}