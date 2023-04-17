package com.trader.joes.service;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trader.joes.fragments.CartFragment;
import com.trader.joes.model.CartItem;
import com.trader.joes.model.Product;
import com.trader.joes.model.User;

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

        CartItem item = new CartItem();
        item.setProductSku(product.getSku());
        item.setPrice(product.getPrice());
        item.setQty(1);

        this.currentUser.addItemToCart(item);
        userDataRef.child("users").child(userId).setValue(this.currentUser);
    }

    public void removeCartItemFromUserCart(CartItem item) {
        System.out.println("Deleting..." + item.getProductSku());
        /*FirebaseUser firebaseUser = authService.getCurrentUser();
        String userId = firebaseUser.getUid();

        CartItem item = new CartItem();
        item.setProductSku(product.getSku());
        item.setPrice(product.getPrice());
        item.setQty(1);

        this.currentUser.addItemToCart(item);
        userDataRef.child("users").child(userId).setValue(this.currentUser);*/
    }
}
