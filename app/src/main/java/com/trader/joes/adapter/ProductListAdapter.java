package com.trader.joes.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.trader.joes.R;
import com.trader.joes.model.Product;
import com.trader.joes.service.UserDataMaintenanceService;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {

    private List<Product> products;
    public Map<String, Product> selectedProducts = new LinkedHashMap<>();

    public ProductListAdapter(List<Product> products) {
        this.products = products;
    }

    public ArrayList<Product> getSelectedProducts() {
        return new ArrayList<>(this.selectedProducts.values());
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
        //new DownloadImageTask(holder.mProductImage).execute(product.getImgURL());
        Picasso.get().load(product.getImgURL()).into(holder.mProductImage);

        holder.mTextViewName.setText(String.valueOf(product.getProductName()));
        holder.mTextViewPrice.setText("$" + product.getPrice());
        //holder.mTextViewDescription.setText(Utility.truncateString(product.getDescription(), 160));
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
        public TextView mTextViewDescription;
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
            mTextViewPrice = itemView.findViewById(R.id.product_price);
            //mTextViewDescription = itemView.findViewById(R.id.product_description);
            mProductImage = itemView.findViewById(R.id.product_img_holder);
            mProductRatingBar = itemView.findViewById(R.id.product_rating_bar);
            mProductRating = itemView.findViewById(R.id.product_rating);
            mNumOfRatings = itemView.findViewById(R.id.num_of_ratings);
            mAddToCartBtn = itemView.findViewById(R.id.add_to_cart_list_view);

            mAddToCartBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getLayoutPosition();
                    Product product = products.get(position);
                    userDataMaintenanceService.addProductToUserCart(product);
                }
            });
        }
    }

    /**
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
