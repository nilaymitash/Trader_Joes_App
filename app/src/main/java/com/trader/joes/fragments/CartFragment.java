package com.trader.joes.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.trader.joes.R;
import com.trader.joes.adapter.CartListAdapter;
import com.trader.joes.model.User;
import com.trader.joes.service.UserDataMaintenanceService;

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

        User currentUser = userDataMaintenanceService.getCurrentUserData();
        mCartListAdapter = new CartListAdapter(currentUser.getCartItems());
        
        mCartListView.setAdapter(mCartListAdapter);

        return view;
    }
}