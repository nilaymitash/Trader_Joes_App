package com.trader.joes.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
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

        Picasso.get().load(this.productsMap.get(item.getProductSku()).getImgURL()).into(holder.mProductImageView);
        holder.mTextViewName.setText(this.productsMap.get(item.getProductSku()).getProductName());
        holder.mTextViewPrice.setText("$" + this.productsMap.get(item.getProductSku()).getPrice());
        holder.qtyInput.setText(String.valueOf(item.getQty()));
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private MaterialCardView mCartItemCard;
        private ImageView mProductImageView;
        private TextView mTextViewName;
        private TextView mTextViewPrice;
        private Button mDecreaseBtn;
        private TextView qtyInput;
        private Button mIncreaseBtn;
        public Button mDeleteItemBtn;
        private UserDataMaintenanceService userDataMaintenanceService;

        public ViewHolder(View itemView) {
            super(itemView);
            userDataMaintenanceService = new UserDataMaintenanceService();

            mCartItemCard = itemView.findViewById(R.id.cart_item_card);
            mProductImageView = itemView.findViewById(R.id.cart_product_img);
            mTextViewName = itemView.findViewById(R.id.cart_product_title);
            mTextViewPrice = itemView.findViewById(R.id.cart_product_price);
            mDecreaseBtn = itemView.findViewById(R.id.decrement_btn);
            qtyInput = itemView.findViewById(R.id.product_qty_input);
            mIncreaseBtn = itemView.findViewById(R.id.increment_btn);
            mDeleteItemBtn = itemView.findViewById(R.id.delete_cart_item_btn);

            mDecreaseBtn.setOnClickListener(ViewHolder.this);
            mIncreaseBtn.setOnClickListener(ViewHolder.this);
            mDeleteItemBtn.setOnClickListener(ViewHolder.this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.delete_cart_item_btn: deleteItem(); break;
                case R.id.decrement_btn: decreaseQty(); break;
                case R.id.increment_btn: increaseQty(); break;
                default: break;
            }
        }

        private void deleteItem() {
            int position = getLayoutPosition();
            CartItem item = cartItems.get(position);
            userDataMaintenanceService.removeCartItemFromUserCart(item);
            cartItems.remove(position);
            notifyDataSetChanged();
        }

        private void decreaseQty() {
            int position = getLayoutPosition();
            CartItem item = cartItems.get(position);
            int currentQty = item.getQty();
            if(currentQty < 2) {
                userDataMaintenanceService.removeCartItemFromUserCart(item);
                cartItems.remove(position);
            } else {
                item.setQty(item.getQty() - 1);
                userDataMaintenanceService.updateItemQty(item);
                cartItems.set(position, item);
            }
            notifyDataSetChanged();
        }

        private void increaseQty() {
            int position = getLayoutPosition();
            CartItem item = cartItems.get(position);
            int currentQty = item.getQty();
            if(currentQty < 999) {
                item.setQty(item.getQty() + 1);
                userDataMaintenanceService.updateItemQty(item);
                cartItems.set(position, item);
            }
            notifyDataSetChanged();
        }
    }

}
