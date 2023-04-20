package com.trader.joes.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.trader.joes.R;
import com.trader.joes.adapter.CartListAdapter;
import com.trader.joes.model.CartItem;
import com.trader.joes.model.User;
import com.trader.joes.service.AuthService;
import com.trader.joes.service.UserDataMaintenanceService;

import java.util.List;
import java.util.function.Consumer;

public class CartFragment extends Fragment {

    private TextView mTotalItems;
    private TextView mSubtotalAmt;
    private CartListAdapter mCartListAdapter;
    private RecyclerView mCartListView;
    private UserDataMaintenanceService userDataMaintenanceService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        mTotalItems = view.findViewById(R.id.total_items);
        mSubtotalAmt = view.findViewById(R.id.subtotal_amount);
        mCartListView = view.findViewById(R.id.cartRecyclerView);

        userDataMaintenanceService = new UserDataMaintenanceService();

        Consumer<User> successConsumer = new Consumer<User>() {
            @Override
            public void accept(User user) {
                List<CartItem> cartItems = user.getCartItems();
                updateCartHeaders(cartItems);

                mCartListAdapter = new CartListAdapter(cartItems);
                mCartListView.setAdapter(mCartListAdapter);
            }
        };
        Consumer<String> failureConsumer = new Consumer<String>() {
            @Override
            public void accept(String unused) {
                Toast.makeText(getActivity(), "DB connection failed!", Toast.LENGTH_SHORT).show();
            }
        };
        userDataMaintenanceService.getCurrentUserData(new AuthService().getCurrentUser().getUid(), successConsumer, failureConsumer);

        return view;
    }

    private void updateCartHeaders(List<CartItem> cartItems){
        int totalItems = 0;
        float subtotalAmt = 0;

        for(CartItem item: cartItems) {
            totalItems += item.getQty();
            subtotalAmt += Float.parseFloat(item.getPrice())*item.getQty();
        }

        mTotalItems.setText(String.valueOf(totalItems) + " items");
        mSubtotalAmt.setText("$" + String.valueOf(subtotalAmt));
    }
}