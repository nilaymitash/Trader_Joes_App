package com.trader.joes.service;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.trader.joes.model.CartItem;
import com.trader.joes.model.Product;
import com.trader.joes.model.User;

public class UserDataMaintenanceService {

    private AuthService authService;
    private final FirebaseDatabase database;
    private final DatabaseReference userDataRef;

    public UserDataMaintenanceService() {
        authService = new AuthService();
        database = FirebaseDatabase.getInstance();
        userDataRef = database.getReference();
    }

    public void addProductToUserCart(Product product) {
        FirebaseUser currentUser = authService.getCurrentUser();
        String userId = currentUser.getUid();

        CartItem item = new CartItem();
        item.setProductSku(product.getSku());
        item.setPrice(product.getPrice());
        item.setQty(1);

        User user = new User();
        user.setuID(userId);
        user.addItemToCart(item);
        userDataRef.child("users").child(userId).setValue(user);
    }
}
