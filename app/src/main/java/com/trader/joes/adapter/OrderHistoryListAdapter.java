package com.trader.joes.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.trader.joes.R;
import com.trader.joes.model.CartItem;
import com.trader.joes.model.Product;
import com.trader.joes.model.Transaction;
import com.trader.joes.service.ProductRetrievalService;

import java.util.List;
import java.util.Map;

public class OrderHistoryListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<Transaction> transactionList;
    private Map<String, Product> productMap;

    public OrderHistoryListAdapter(Context context, List<Transaction> transactionList) {
        this.context = context;
        this.transactionList = transactionList;
        this.productMap = new ProductRetrievalService().getAllProductsMap();
    }

    @Override
    public int getGroupCount() {
        return transactionList.size();
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.transactionList.get(listPosition).getItems().size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.transactionList.get(listPosition);
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.transactionList.get(listPosition).getItems()
                .get(expandedListPosition);
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        Transaction transaction = (Transaction) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.order_list_group, null);
        }
        TextView dateView = (TextView) convertView
                .findViewById(R.id.order_date);
        TextView amountView = (TextView) convertView
                .findViewById(R.id.order_amount);
        TextView idView = (TextView) convertView
                .findViewById(R.id.order_id);

        dateView.setTypeface(null, Typeface.BOLD);
        amountView.setTypeface(null, Typeface.BOLD);
        idView.setTypeface(null, Typeface.BOLD);

        dateView.setText(transaction.getTransactionDate());
        amountView.setText(transaction.getTransactionAmt());
        idView.setText(transaction.getTransactionId());
        return convertView;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        CartItem item = (CartItem) getChild(listPosition, expandedListPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.order_list_item, null);
        }

        ImageView productImgView = convertView.findViewById(R.id.order_history_product_img);
        TextView productNameView = convertView.findViewById(R.id.order_history_product_name);
        TextView qtyView = convertView.findViewById(R.id.order_history_product_qty);
        TextView priceView = convertView.findViewById(R.id.order_history_product_price);

        Picasso.get().load(this.productMap.get(item.getProductSku()).getImgURL()).into(productImgView);
        productNameView.setText(this.productMap.get(item.getProductSku()).getProductName());
        qtyView.setText(String.valueOf(item.getQty()));
        priceView.setText("$" + this.productMap.get(item.getProductSku()).getPrice());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
}
