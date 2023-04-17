package com.trader.joes.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.trader.joes.R;
import com.trader.joes.model.CartItem;
import com.trader.joes.model.Product;
import com.trader.joes.service.ProductRetrievalService;
import com.trader.joes.service.UserDataMaintenanceService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.ViewHolder> {

    private List<CartItem> cartItems;

    private Map<String, Product> productsMap = new LinkedHashMap<>();

    public CartListAdapter(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public CartListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_list_item,
                parent, false);
        this.productsMap = ProductRetrievalService.getAllProductsMap();
        return new CartListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartListAdapter.ViewHolder holder, int position) {
        CartItem item = cartItems.get(position);

        holder.mTextViewName.setText(this.productsMap.get(item.getProductSku()).getProductName());
        holder.mTextViewPrice.setText("$" + this.productsMap.get(item.getProductSku()).getPrice());
        holder.qtyInput.setText(String.valueOf(item.getQty()));
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextViewName;
        public TextView mTextViewPrice;
        public EditText qtyInput;
        public Button mdeleteItemBtn;
        private UserDataMaintenanceService userDataMaintenanceService;

        public ViewHolder(View itemView) {
            super(itemView);
            userDataMaintenanceService = new UserDataMaintenanceService();

            mTextViewName = itemView.findViewById(R.id.product_title);
            mTextViewPrice = itemView.findViewById(R.id.product_price);
            qtyInput = itemView.findViewById(R.id.product_qty_input);
            mdeleteItemBtn = itemView.findViewById(R.id.delete_cart_item_btn);

            mdeleteItemBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getLayoutPosition();
                    CartItem item = cartItems.get(position);
                    userDataMaintenanceService.removeCartItemFromUserCart(item);
                    cartItems.remove(position);
                    notifyDataSetChanged();
                }
            });
        }
    }
}
