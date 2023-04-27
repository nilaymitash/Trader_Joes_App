package com.trader.joes.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.trader.joes.R;
import com.trader.joes.adapter.CartListAdapter;
import com.trader.joes.model.CardPattern;
import com.trader.joes.model.CartItem;
import com.trader.joes.model.Transaction;
import com.trader.joes.model.User;
import com.trader.joes.service.AuthService;
import com.trader.joes.service.UserDataMaintenanceService;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

/**
 * Cart fragment displays user's shopping cart
 */
public class CartFragment extends Fragment {

    private RelativeLayout mainLayout;
    private UserDataMaintenanceService userDataMaintenanceService;
    private CartListAdapter mCartListAdapter;
    private TextView mTotalItems;
    private TextView mSubtotalAmt;
    private Button mProceedToCheckoutBtn;
    private Button mContinueShoppingBtn;
    private Button mConfirmPaymentBtn;
    private Button mBackToCartBtn;
    private RecyclerView mCartListView;
    private View mPaymentLayout;
    private EditText mNameInput;
    private EditText mCardNumInput;
    private EditText mMonthInput;
    private EditText mYearInput;
    private EditText mCVVInput;
    private ImageView mCardTypeImg;
    private TextView mNameValidation;
    private TextView mCardNumValidation;
    private TextView mDateValidation;
    private TextView mCVVValidation;
    private List<CartItem> currentCartItems = new ArrayList<>();
    private List<CardPattern> listOfPattern = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        userDataMaintenanceService = new UserDataMaintenanceService();
        mainLayout = view.findViewById(R.id.cart_layout);
        mTotalItems = view.findViewById(R.id.total_items);
        mSubtotalAmt = view.findViewById(R.id.subtotal_amount);
        mCartListView = view.findViewById(R.id.cartRecyclerView);
        mProceedToCheckoutBtn = view.findViewById(R.id.proceed_to_checkout_btn);
        mContinueShoppingBtn = view.findViewById(R.id.continue_shopping_btn);
        mConfirmPaymentBtn = view.findViewById(R.id.confirm_payment);
        mBackToCartBtn = view.findViewById(R.id.back_to_cart);
        mPaymentLayout = view.findViewById(R.id.payment_layout);
        mNameInput = view.findViewById(R.id.card_holder_input);
        mCardNumInput = view.findViewById(R.id.card_number_input);
        mMonthInput = view.findViewById(R.id.month_input);
        mYearInput = view.findViewById(R.id.year_input);
        mCVVInput = view.findViewById(R.id.cvv_input);
        mCardTypeImg = view.findViewById(R.id.card_type_img);
        mNameValidation = view.findViewById(R.id.card_holder_validation_msg);
        mCardNumValidation = view.findViewById(R.id.card_number_validation_msg);
        mDateValidation = view.findViewById(R.id.date_validation_msg);
        mCVVValidation = view.findViewById(R.id.cvv_validation_msg);

        //populate all card patterns
        listOfPattern = new CardPattern().getAllPatterns();

        //add on click listeners
        mProceedToCheckoutBtn.setOnClickListener(new CartFragmentListener());
        mContinueShoppingBtn.setOnClickListener(new CartFragmentListener());
        mConfirmPaymentBtn.setOnClickListener(new CartFragmentListener());
        mBackToCartBtn.setOnClickListener(new CartFragmentListener());


        //hide validation messages on load.
        mNameValidation.setVisibility(View.GONE);
        mCardNumValidation.setVisibility(View.GONE);
        mDateValidation.setVisibility(View.GONE);
        mCVVValidation.setVisibility(View.GONE);

        /**
         * TODO:
         * text change listener for card number input
         * to format and identify card provider
         */
        mCardNumInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String ccNum = editable.toString();
                for(CardPattern pattern:listOfPattern){
                    if(ccNum.matches(pattern.getPattern())){
                        mCardTypeImg.setImageResource(pattern.getDrawableImg());
                        break;
                    } else {
                        mCardTypeImg.setImageResource(0);
                    }
                }
            }
        });


        /**
         * success callback function for fetching user's data
         */
        Consumer<User> successConsumer = new Consumer<User>() {
            @Override
            public void accept(User user) {
                List<CartItem> cartItems = user.getCartItems();
                currentCartItems = cartItems;
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

        //hide proceed to checkout button if cart is empty
        if(totalItems == 0) {
            mProceedToCheckoutBtn.setVisibility(View.GONE);
        } else {
            mProceedToCheckoutBtn.setVisibility(View.VISIBLE);
        }
    }

    private class CartFragmentListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.proceed_to_checkout_btn: proceedToCheckout();
                break;
                case R.id.continue_shopping_btn: continueShopping();
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

        private void continueShopping() {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProductListFragment()).commit();
        }

        private void confirmPayment() {
            //hide the keyboard
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0); //hide numeric keyboard

            //validate all inputs
            validateExpirationDate();
            Transaction transaction = populateTransactionObject();

            Consumer<Transaction> successCallback = new Consumer<Transaction>() {
                @Override
                public void accept(Transaction transaction) {
                    //updated transaction object with transaction id.
                    //navigate to order confirmation fragment and pass transaction id
                    Bundle bundle = new Bundle();
                    bundle.putString("ORDER_CONFIRMATION_NUM", transaction.getTransactionId());

                    OrderConfirmationFragment orderConfirmationFragment = new OrderConfirmationFragment();
                    orderConfirmationFragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, orderConfirmationFragment).commit();
                }
            };
            userDataMaintenanceService.completeTransaction(transaction, successCallback);
        }

        private Transaction populateTransactionObject() {
            Calendar currentDate = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
            String currentDateStr = dateFormat.format(currentDate.getTime());

            Transaction transaction = new Transaction();
            transaction.setItems(currentCartItems);
            transaction.setTransactionAmt(String.valueOf(mSubtotalAmt.getText()));
            transaction.setTransactionDate(currentDateStr);
            return transaction;
        }

        private void validateExpirationDate() {
            String monthStr = String.valueOf(mMonthInput.getText());
            String yearStr = String.valueOf(mYearInput.getText());
            int mm = 0;
            int yyyy = 0;
            if(monthStr != null && monthStr != "null" && !monthStr.trim().equals("")) {
                mm = Integer.parseInt(monthStr);
            }

            if(yearStr != null && yearStr != "null" && !yearStr.trim().equals("")) {
                yyyy = Integer.parseInt(yearStr);
            }

            Calendar currentDate = Calendar.getInstance();
            int currentYear = currentDate.get(Calendar.YEAR);
            int currentMonth = currentDate.get(Calendar.MONTH) + 1;

            if(mm > 12 || mm < 1) {
                mDateValidation.setVisibility(View.VISIBLE);
            } else if(yyyy < currentYear) {
                mDateValidation.setVisibility(View.VISIBLE);
            } else if(mm < currentMonth && yyyy < currentYear) {
                mDateValidation.setVisibility(View.VISIBLE);
            } else {
                mDateValidation.setVisibility(View.GONE);
            }
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