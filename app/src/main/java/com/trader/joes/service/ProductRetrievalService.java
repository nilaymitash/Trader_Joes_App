package com.trader.joes.service;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trader.joes.model.Product;
import com.trader.joes.model.ProductFilter;
import com.trader.joes.model.Review;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * This service is responsible for handling any data manipulation related to the products
 */
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

    /**
     * This method fetches all products from firebase
     * It requires the caller to provide 1 success and 1 failure callback function
     * @param success
     * @param failure
     */
    public void getAllProducts(Consumer<List<Product>> success, Consumer<DatabaseError> failure) {

        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Set<String> productCharacteristics = new LinkedHashSet<>();
                for(DataSnapshot ds : snapshot.getChildren()) {
                    Product product = ds.getValue(Product.class);
                    String sku = ds.getKey();
                    allProductsMap.put(sku, product);
                    for(String s: product.getProductCharacteristics()) {
                        productCharacteristics.add(s);
                    }
                }
                new ProductFilter().setFilterOptions(productCharacteristics);

                //Execute success callback function provided by the caller
                success.accept(new ArrayList<>(allProductsMap.values()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Execute failure callback function provided by the caller
                failure.accept(error);
            }
        });
    }

    public void addReview(String productId, Review review) {
        FirebaseUser user = new AuthService().getCurrentUser();
        String userId = user.getUid();

        review.setUserId(userId);

        productsRef.child(productId).child("reviews").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                List<Review> reviews = (List<Review>)dataSnapshot.getValue();
                if(reviews == null) {
                    //product has no reviews. Create new list
                    List<Review> newReviewList = new ArrayList<>();
                    newReviewList.add(review);
                    productsRef.child(productId).child("reviews").setValue(newReviewList);
                } else {
                    //product has review. Add new review and save
                    reviews.add(review);
                    productsRef.child(productId).child("reviews").setValue(reviews);
                }
            }
        });
    }
}
