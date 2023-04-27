package com.trader.joes.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.trader.joes.R;

public class OrderConfirmationFragment extends Fragment {

    private TextView mConfirmationNumber;
    private Button mContinueShopping;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_confirmation, container, false);
        mConfirmationNumber = view.findViewById(R.id.confirmation_number);
        mContinueShopping = view.findViewById(R.id.continue_shopping_btn);

        String orderConfirmationNumber = getArguments().getString("ORDER_CONFIRMATION_NUM");
        mConfirmationNumber.setText(orderConfirmationNumber); //set confirmation number

        mContinueShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProductListFragment()).commit();
            }
        });
        return view;
    }
}