package com.trader.joes.service;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trader.joes.model.CartItem;
import com.trader.joes.model.Product;
import com.trader.joes.model.User;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class UserDataMaintenanceService {

    private AuthService authService;
    private final FirebaseDatabase database;
    private final DatabaseReference userDataRef;

    private static User currentUser;

    public UserDataMaintenanceService() {
        authService = new AuthService();
        database = FirebaseDatabase.getInstance();
        userDataRef = database.getReference();
    }

    public static User getCurrentUserData() {
        return currentUser;
    }

    public void getCurrentUserData(String userId, Consumer<User> successCallback, Consumer<String> failureCallback) {
        userDataRef.child("users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                successCallback.accept(user);
                currentUser = user;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                failureCallback.accept(error.getMessage());
            }
        });
    }

    public void addProductToUserCart(Product product) {
        FirebaseUser firebaseUser = authService.getCurrentUser();
        String userId = firebaseUser.getUid();

        if(currentUser == null) {
            currentUser = new User();
            currentUser.setuID(userId);
        }

        Map<String, CartItem> newCartMap = populateMap();
        if(currentUser.getCartItems() != null) {
            if(newCartMap.containsKey(product.getSku())) {
                newCartMap.get(product.getSku()).setQty(newCartMap.get(product.getSku()).getQty() + 1);
            } else {
                CartItem item = new CartItem();
                item.setProductSku(product.getSku());
                item.setPrice(product.getPrice());
                item.setQty(1);
                newCartMap.put(product.getSku(), item);
            }
        }

        this.currentUser.setCartItems(new ArrayList<>(newCartMap.values()));
        userDataRef.child("users").child(userId).setValue(this.currentUser);
    }

    private Map<String, CartItem> populateMap() {
        Map<String, CartItem> newCartMap = new LinkedHashMap<>();

        if(currentUser.getCartItems() != null) {
            for (CartItem item: currentUser.getCartItems()) {
                newCartMap.put(item.getProductSku(), item);
            }
        }

        return newCartMap;
    }

    public void removeCartItemFromUserCart(CartItem item) {
        FirebaseUser firebaseUser = authService.getCurrentUser();
        String userId = firebaseUser.getUid();

        Map<String, CartItem> newCartMap = populateMap();
        if(newCartMap.containsKey(item.getProductSku())) {
            newCartMap.remove(item.getProductSku());
        }

        userDataRef.child("users").child(userId).child("cartItems").setValue(new ArrayList<>(newCartMap.values()));

        /*this.currentUser.setCartItems(new ArrayList<>(newCartMap.values()));
        userDataRef.child("users").child(userId).setValue(this.currentUser);*/
    }
}
