package com.trader.joes.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.trader.joes.R;
import com.trader.joes.adapter.ReviewListAdapter;
import com.trader.joes.model.Product;
import com.trader.joes.model.Review;
import com.trader.joes.service.ProductRetrievalService;
import com.trader.joes.service.UserDataMaintenanceService;
import com.trader.joes.service.UtilityService;

import java.util.List;
import java.util.function.Consumer;

/**
 * This fragment is to display 1 product information at a time.
 */
public class ProductViewFragment extends Fragment {

    private RelativeLayout mainLayout;
    private TextView mProductNameView;
    private RatingBar mRatingBar;
    private TextView mNumOfRatingsLabel;
    private ImageView mProductImage;
    private TextView mPriceLabel;
    private Button mAddToCartBtn;
    private Button mViewCartBtn;
    private Button mLeaveReviewBtn;
    private RelativeLayout mReviewLayout;
    private Button mSubmitReviewBtn;
    private Button mCancelReviewBtn;
    private RatingBar mReviewRatingBar;
    private EditText mReviewInput;
    private UserDataMaintenanceService userDataMaintenanceService;
    private ProductRetrievalService productRetrievalService;
    private RecyclerView reviewRecyclerView;
    Product selectedProduct;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_view, container, false);

        userDataMaintenanceService = new UserDataMaintenanceService();
        productRetrievalService = new ProductRetrievalService();

        mainLayout = view.findViewById(R.id.product_view_layout);
        mProductNameView = view.findViewById(R.id.product_title_label);
        mRatingBar = view.findViewById(R.id.product_rating_bar);
        mNumOfRatingsLabel = view.findViewById(R.id.num_of_ratings);
        mProductImage = view.findViewById(R.id.product_img_holder);
        mPriceLabel = view.findViewById(R.id.product_price_label);
        mAddToCartBtn = view.findViewById(R.id.add_to_cart_product_page);
        mViewCartBtn = view.findViewById(R.id.view_cart_product_page);
        mLeaveReviewBtn = view.findViewById(R.id.leave_review_btn);
        mReviewLayout = view.findViewById(R.id.review_layout);
        mSubmitReviewBtn = view.findViewById(R.id.submit_review_btn);
        mCancelReviewBtn = view.findViewById(R.id.cancel_review);
        mReviewRatingBar = view.findViewById(R.id.review_rating);
        mReviewInput = view.findViewById(R.id.review_input);
        reviewRecyclerView = view.findViewById(R.id.review_list);

        this.selectedProduct = (Product)getArguments().getSerializable("SELECTED_PRODUCT");

        mLeaveReviewBtn.setOnClickListener(new ProductViewListener());
        mSubmitReviewBtn.setOnClickListener(new ProductViewListener());
        mCancelReviewBtn.setOnClickListener(new ProductViewListener());

        Consumer<List<Review>> successCallback = new Consumer<List<Review>>() {
            @Override
            public void accept(List<Review> reviews) {
                ReviewListAdapter reviewListAdapter = new ReviewListAdapter(reviews);
                reviewRecyclerView.setAdapter(reviewListAdapter);
                //update review when new reviews are added
                updateReviewAndRating(reviews);
            }
        };
        new ProductRetrievalService().getReviewList(this.selectedProduct.getSku(), successCallback);

        populateViewData();

        return view;
    }

    private void populateViewData() {
        mProductNameView.setText(this.selectedProduct.getProductName());
        Picasso.get().load(this.selectedProduct.getImgURL()).into(mProductImage);
        mPriceLabel.setText("$" + this.selectedProduct.getPrice());
        mAddToCartBtn.setOnClickListener(new ProductViewListener());
        mViewCartBtn.setOnClickListener(new ProductViewListener());
        mRatingBar.setRating(this.selectedProduct.getRating());
        mNumOfRatingsLabel.setText("(" + this.selectedProduct.getNumOfRatings() + ")");
    }

    private void updateReviewAndRating(List<Review> reviews) {
        this.selectedProduct.setReviews(reviews);
        mRatingBar.setRating(this.selectedProduct.getRating());
        mNumOfRatingsLabel.setText("(" + this.selectedProduct.getNumOfRatings() + ")");
    }

    private class ProductViewListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.add_to_cart_product_page: addToCart();
                break;
                case R.id.view_cart_product_page: viewCart();
                break;
                case R.id.leave_review_btn: leaveReview();
                break;
                case R.id.submit_review_btn: submitReview();
                break;
                case R.id.cancel_review: closeReview();
            }
        }

        private void addToCart() {
            //add product to the cart
            userDataMaintenanceService.addProductToUserCart(selectedProduct);
        }

        private void viewCart() {
            UtilityService.hideKeyboard(getActivity(), mainLayout);
            //navigate user to Cart view on click of "View cart" button
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CartFragment()).commit();
        }

        private void leaveReview() {
            //Hide leave review button
            mLeaveReviewBtn.setVisibility(View.GONE);
            //show review form
            mReviewLayout.setVisibility(View.VISIBLE);
        }

        private void submitReview() {
            UtilityService.hideKeyboard(getActivity(), mainLayout);

            float rating = mReviewRatingBar.getRating();
            String reviewText = String.valueOf(mReviewInput.getText());

            Review review = new Review();
            review.setRating(rating);
            review.setReview(reviewText);
            productRetrievalService.addReview(selectedProduct.getSku(), review);
            closeReview();
            Toast.makeText(getActivity(), "Review submitted successfully!", Toast.LENGTH_SHORT).show();
        }

        private void closeReview() {
            UtilityService.hideKeyboard(getActivity(), mainLayout);

            mReviewRatingBar.setRating(5f);
            mReviewInput.setText("");

            //Show leave review button
            mLeaveReviewBtn.setVisibility(View.VISIBLE);
            //Hide review form
            mReviewLayout.setVisibility(View.GONE);
        }
    }
}