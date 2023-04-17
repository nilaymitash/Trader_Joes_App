package com.trader.joes.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.trader.joes.R;
import com.trader.joes.adapter.CartListAdapter;
import com.trader.joes.adapter.ProductListAdapter;
import com.trader.joes.model.Product;
import com.trader.joes.model.User;
import com.trader.joes.service.ProductRetrievalService;
import com.trader.joes.service.UserDataMaintenanceService;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CartFragment extends Fragment {

    private CartListAdapter mCartListAdapter;
    private RecyclerView mCartListView;
    private UserDataMaintenanceService userDataMaintenanceService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        mCartListView = view.findViewById(R.id.cartRecyclerView);

        userDataMaintenanceService = new UserDataMaintenanceService();

        Consumer<List<Product>> productConsumer = new Consumer<List<Product>>() {
            @Override
            public void accept(List<Product> products) {

            }
        };

        Consumer<DatabaseError> dbErrorConsumer = new Consumer<DatabaseError>() {
            @Override
            public void accept(DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
        User currentuser = userDataMaintenanceService.getCurrentUserData();
        mCartListAdapter = new CartListAdapter(currentuser.getCartItems());
        mCartListView.setAdapter(mCartListAdapter);

        return view;
    }
}