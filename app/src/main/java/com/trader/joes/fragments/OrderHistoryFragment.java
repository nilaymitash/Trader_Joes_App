package com.trader.joes.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.trader.joes.R;
import com.trader.joes.adapter.OrderHistoryListAdapter;
import com.trader.joes.model.Transaction;
import com.trader.joes.model.TransactionHistory;
import com.trader.joes.service.UserDataMaintenanceService;

import java.util.List;
import java.util.function.Consumer;

public class OrderHistoryFragment extends Fragment {

    private ExpandableListView expandableListView;
    private OrderHistoryListAdapter expandableListAdapter;
    private UserDataMaintenanceService userDataMaintenanceService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_history, container, false);

        userDataMaintenanceService = new UserDataMaintenanceService();

        //initialize UI components
        expandableListView = view.findViewById(R.id.order_history_list);

        fetchOrderHistory();
        return view;
    }

    private void fetchOrderHistory() {
        Consumer<TransactionHistory> successCallback = new Consumer<TransactionHistory>() {
            @Override
            public void accept(TransactionHistory transactionHistory) {
                //populate expandable list data and adapter
                List<Transaction> txnList = transactionHistory.getTransactionList();
                expandableListAdapter = new OrderHistoryListAdapter(getActivity(), txnList);
                expandableListView.setAdapter(expandableListAdapter);
            }
        };

        Consumer<String> failureCallback = new Consumer<String>() {
            @Override
            public void accept(String s) {
                Toast.makeText(getActivity(), "Failed to fetch order history", Toast.LENGTH_SHORT).show();
            }
        };

        userDataMaintenanceService.getOrderHistory(successCallback, failureCallback);
    }
}