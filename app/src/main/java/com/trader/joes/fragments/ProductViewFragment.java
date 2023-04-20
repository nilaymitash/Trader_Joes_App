package com.trader.joes.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.trader.joes.R;
import com.trader.joes.model.Product;
import com.trader.joes.service.UserDataMaintenanceService;

public class ProductViewFragment extends Fragment {

    private TextView mProductNameView;
    private RatingBar mRatingBar;
    private TextView mNumOfRatingsLabel;
    private ImageView mProductImage;
    private TextView mPriceLabel;
    private Button mAddToCartBtn;
    private Button mViewCartBtn;
    private UserDataMaintenanceService userDataMaintenanceService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_view, container, false);

        mProductNameView = view.findViewById(R.id.product_title_label);
        mRatingBar = view.findViewById(R.id.product_rating_bar);
        mNumOfRatingsLabel = view.findViewById(R.id.num_of_ratings);
        mProductImage = view.findViewById(R.id.product_img_holder);
        mPriceLabel = view.findViewById(R.id.product_price_label);
        mAddToCartBtn = view.findViewById(R.id.add_to_cart_product_page);
        mViewCartBtn = view.findViewById(R.id.view_cart_product_page);
        userDataMaintenanceService = new UserDataMaintenanceService();

        Product selectedProduct = (Product)getArguments().getSerializable("SELECTED_PRODUCT");
        populateViewData(selectedProduct);
        return view;
    }

    private void populateViewData(Product product) {
        mProductNameView.setText(product.getProductName());
        Picasso.get().load(product.getImgURL()).into(mProductImage);
        mPriceLabel.setText(product.getPrice());
        mAddToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userDataMaintenanceService.addProductToUserCart(product);
            }
        });
        mViewCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CartFragment()).commit();
            }
        });
    }
}