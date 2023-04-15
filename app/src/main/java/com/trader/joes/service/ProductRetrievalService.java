package com.trader.joes.service;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.trader.joes.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductRetrievalService {

    private final FirebaseDatabase database;
    private final DatabaseReference productsRef;

    public ProductRetrievalService() {
        database = FirebaseDatabase.getInstance();
        productsRef = database.getReference("products");
    }

    public List<Product> getAllProducts() {
        return new ArrayList<>();
    }
}
