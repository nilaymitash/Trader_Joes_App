package com.trader.joes.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.trader.joes.R;
import com.trader.joes.adapter.CartListAdapter;
import com.trader.joes.model.CartItem;
import com.trader.joes.model.User;
import com.trader.joes.service.AuthService;
import com.trader.joes.service.UserDataMaintenanceService;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.function.Consumer;

/**
 * Cart fragment displays user's shopping cart
 */
public class CartFragment extends Fragment {

    private UserDataMaintenanceService userDataMaintenanceService;
    private CartListAdapter mCartListAdapter;
    private TextView mTotalItems;
    private TextView mSubtotalAmt;
    private Button mProceedToCheckoutBtn;
    private Button mConfirmPaymentBtn;
    private Button mBackToCartBtn;
    private RecyclerView mCartListView;
    private View mPaymentLayout;
    private TextInputEditText mCardholderNameInput;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        userDataMaintenanceService = new UserDataMaintenanceService();
        mTotalItems = view.findViewById(R.id.total_items);
        mSubtotalAmt = view.findViewById(R.id.subtotal_amount);
        mCartListView = view.findViewById(R.id.cartRecyclerView);
        mProceedToCheckoutBtn = view.findViewById(R.id.proceed_to_checkout_btn);
        mConfirmPaymentBtn = view.findViewById(R.id.confirm_payment);
        mBackToCartBtn = view.findViewById(R.id.back_to_cart);
        mPaymentLayout = view.findViewById(R.id.payment_layout);
        mCardholderNameInput = view.findViewById(R.id.card_holder_input);

        //add on click listeners
        mProceedToCheckoutBtn.setOnClickListener(new CartFragmentListener());
        mConfirmPaymentBtn.setOnClickListener(new CartFragmentListener());
        mBackToCartBtn.setOnClickListener(new CartFragmentListener());

        /**
         * success callback function for fetching user's data
         */
        Consumer<User> successConsumer = new Consumer<User>() {
            @Override
            public void accept(User user) {
                List<CartItem> cartItems = user.getCartItems();
                updateCartHeaders(cartItems);

                mCartListAdapter = new CartListAdapter(cartItems, CartFragment.this);
                mCartListView.setAdapter(mCartListAdapter);
            }
        };

        /**
         * failure callback function for fetching user's data
         */
        Consumer<String> failureConsumer = new Consumer<String>() {
            @Override
            public void accept(String unused) {
                Toast.makeText(getActivity(), "DB connection failed!", Toast.LENGTH_SHORT).show();
            }
        };

        //get user's cart data on create of the fragment
        userDataMaintenanceService.getCurrentUserData(new AuthService().getCurrentUser().getUid(), successConsumer, failureConsumer);

        return view;
    }

    /**
     * This method populates data for Cart header
     * @param cartItems
     */
    private void updateCartHeaders(List<CartItem> cartItems){
        int totalItems = 0;
        float subtotalAmt = 0;
        DecimalFormat df = new DecimalFormat("0.00");
        df.setMaximumFractionDigits(2);

        for(CartItem item: cartItems) {
            totalItems += item.getQty();
            subtotalAmt += Float.parseFloat(item.getPrice())*item.getQty();
        }

        mTotalItems.setText(totalItems + " items");
        mSubtotalAmt.setText("$" + df.format(subtotalAmt));
    }

    private class CartFragmentListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.proceed_to_checkout_btn: proceedToCheckout();
                break;
                case R.id.confirm_payment: confirmPayment();
                break;
                case R.id.back_to_cart: backToCart();
                break;
                default: break;
            }
        }

        private void proceedToCheckout() {
            //hide checkout btn, and cart items
            mProceedToCheckoutBtn.setVisibility(View.GONE);
            mCartListView.setVisibility(View.GONE);

            //show payment layout, and payment buttons
            mPaymentLayout.setVisibility(View.VISIBLE);
            mConfirmPaymentBtn.setVisibility(View.VISIBLE);
            mBackToCartBtn.setVisibility(View.VISIBLE);
        }

        private void confirmPayment() {

        }

        private void backToCart() {
            //show checkout btn, and cart items
            mProceedToCheckoutBtn.setVisibility(View.VISIBLE);
            mCartListView.setVisibility(View.VISIBLE);

            //hide payment layout, and payment buttons
            mPaymentLayout.setVisibility(View.GONE);
            mConfirmPaymentBtn.setVisibility(View.GONE);
            mBackToCartBtn.setVisibility(View.GONE);
        }
    }
}