package com.trader.joes.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.trader.joes.R;
import com.trader.joes.model.CartItem;
import com.trader.joes.model.Transaction;

import java.util.HashMap;
import java.util.List;

public class OrderHistoryListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<Transaction> transactionList;

    public OrderHistoryListAdapter(Context context, List<Transaction> transactionList) {
        this.context = context;
        this.transactionList = transactionList;
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
        return this.transactionList.get(listPosition).getTransactionId();
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
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.order_list_group, null);
        }
        TextView listTitleTextView = (TextView) convertView
                .findViewById(R.id.order_group_title);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);
        return convertView;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        CartItem item = (CartItem) getChild(listPosition, expandedListPosition);
        String expandedListText = item.getProductSku() + " | " + item.getQty() + " | " + item.getPrice();

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.order_list_item, null);
        }
        TextView expandedListTextView = (TextView) convertView
                .findViewById(R.id.expanded_order_list_item);
        expandedListTextView.setText(expandedListText);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
}
