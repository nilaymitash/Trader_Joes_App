package com.trader.joes.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.trader.joes.R;
import com.trader.joes.fragments.ProductViewFragment;
import com.trader.joes.model.Product;
import com.trader.joes.service.UserDataMaintenanceService;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for Product list RecyclerView
 */
public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {

    private List<Product> products;
    private Fragment fragment;

    public ProductListAdapter(List<Product> products, Fragment fragment) {
        this.products = products;
        this.fragment = fragment;
    }

    public void filterList(ArrayList<Product> filteredList) {
        products = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_item,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = products.get(position);

        //Download product image:
        Picasso.get().load(product.getImgURL()).into(holder.mProductImage);

        holder.mTextViewName.setText(String.valueOf(product.getProductName()));
        holder.mTextViewPrice.setText("$" + product.getPrice());
        holder.mProductRating.setText(String.valueOf(product.getRating()));
        holder.mProductRatingBar.setRating(product.getRating());
        holder.mNumOfRatings.setText("(" + product.getNumOfRatings() + ")");
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextViewName;
        public TextView mTextViewPrice;
        public ImageView mProductImage;
        public RatingBar mProductRatingBar;
        public TextView mProductRating;
        public TextView mNumOfRatings;
        public Button mAddToCartBtn;
        private UserDataMaintenanceService userDataMaintenanceService;

        public ViewHolder(View itemView) {
            super(itemView);
            userDataMaintenanceService = new UserDataMaintenanceService();

            mTextViewName = itemView.findViewById(R.id.product_title);
            mTextViewPrice = itemView.findViewById(R.id.cart_product_price);
            mProductImage = itemView.findViewById(R.id.product_img_holder);
            mProductRatingBar = itemView.findViewById(R.id.product_rating_bar);
            mProductRating = itemView.findViewById(R.id.product_rating);
            mNumOfRatings = itemView.findViewById(R.id.num_of_ratings);
            mAddToCartBtn = itemView.findViewById(R.id.add_to_cart_list_view);

            /**
             * On click of add to cart button, add the item to the user's cart
             */
            mAddToCartBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getLayoutPosition();
                    Product product = products.get(position);
                    userDataMaintenanceService.addProductToUserCart(product);
                }
            });

            /**
             * On click of entire card/item view, navigate the user to the product page
             */
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("SELECTED_PRODUCT", products.get(getLayoutPosition()));

                    ProductViewFragment pvFragment = new ProductViewFragment();
                    pvFragment.setArguments(bundle);
                    fragment.getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, pvFragment).commit();
                }
            });
        }
    }

    /**
     * @Deprecated
     * This async download task has been replace by Picasso library.
     */
    @Deprecated
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap bmp = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bmp = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bmp;
        }
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
