package com.trader.joes.service;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trader.joes.model.Product;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ProductRetrievalService {

    private final FirebaseDatabase database;
    private final DatabaseReference productsRef;
    private static Map<String, Product> allProductsMap = new LinkedHashMap<>();

    public ProductRetrievalService() {
        database = FirebaseDatabase.getInstance();
        productsRef = database.getReference("products");
    }

    /**
     * Used by the Cart fragment
     * to retrieve product name using SKU
     * @return
     */
    public static Map<String, Product> getAllProductsMap() {
        return allProductsMap;
    }

    public void getAllProducts(Consumer<List<Product>> success, Consumer<DatabaseError> failure) {

        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Product> allProducts = new ArrayList<>();
                for(DataSnapshot ds : snapshot.getChildren()) {
                    Product product = ds.getValue(Product.class);
                    allProducts.add(product);
                    allProductsMap.put(product.getSku(), product);
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
