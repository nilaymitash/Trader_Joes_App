package com.trader.joes.service;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.trader.joes.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ProductRetrievalService {

    private final FirebaseDatabase database;
    private final DatabaseReference productsRef;

    public ProductRetrievalService() {
        database = FirebaseDatabase.getInstance();
        productsRef = database.getReference("products");
    }

    public void getAllProducts(Consumer<List<Product>> success, Consumer<DatabaseError> failure) {

        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Product> allProducts = new ArrayList<>();
                for(DataSnapshot ds : snapshot.getChildren()) {
                    allProducts.add(ds.getValue(Product.class));
                }
                success.accept(allProducts);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                failure.accept(error);
            }
        });
    }
}
