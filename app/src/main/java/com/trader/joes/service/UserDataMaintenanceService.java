package com.trader.joes.service;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trader.joes.model.CartItem;
import com.trader.joes.model.Product;
import com.trader.joes.model.Transaction;
import com.trader.joes.model.TransactionHistory;
import com.trader.joes.model.User;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;

/**
 * This service is responsible for handling
 * all User data related transactions.
 */
public class UserDataMaintenanceService {

    private AuthService authService;
    private final FirebaseDatabase database;
    private final DatabaseReference userDataRef;
    private static final String ID_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    private static User currentUser;

    public UserDataMaintenanceService() {
        authService = new AuthService();
        database = FirebaseDatabase.getInstance();
        userDataRef = database.getReference();
    }

    public static User getCurrentUserData() {
        return currentUser;
    }

    /**
     * This method fetches the data pertaining to the user who is currently logged in
     * Requires the caller to provide 1 success and 1 failure callback function.
     * @param userId
     * @param successCallback
     * @param failureCallback
     */
    public void getCurrentUserData(String userId, Consumer<User> successCallback, Consumer<String> failureCallback) {
        userDataRef.child("users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                //execute success callback function provided by the caller
                successCallback.accept(user);
                currentUser = user;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //execute failure callback function provided by the caller
                failureCallback.accept(error.getMessage());
            }
        });
    }

    /**
     * Adds a given product to the user's cart
     * @param product
     */
    public void addProductToUserCart(Product product) {
        //Get currently logged in user via Auth service
        FirebaseUser firebaseUser = authService.getCurrentUser();
        String userId = firebaseUser.getUid();

        if(currentUser == null) {
            currentUser = new User();
            currentUser.setuID(userId);
        }

        //Convert list of cart items to a map
        Map<String, CartItem> newCartMap = populateMap();

        //Update cart - either add a new product or update a product's quantity
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

        //update the current user object in memory
        this.currentUser.setCartItems(new ArrayList<>(newCartMap.values()));

        //saves/updates current user's data
        userDataRef.child("users").child(userId).setValue(this.currentUser);
    }

    /**
     * This method acts as a transformer
     * It transforms current Cart items into a Map
     * So that its easier to update quantities of items if needed
     * @return
     */
    private Map<String, CartItem> populateMap() {
        Map<String, CartItem> newCartMap = new LinkedHashMap<>();

        if(currentUser.getCartItems() != null) {
            for (CartItem item: currentUser.getCartItems()) {
                newCartMap.put(item.getProductSku(), item);
            }
        }

        return newCartMap;
    }

    /**
     * This method removes an item from the cart
     * @param item
     */
    public void removeCartItemFromUserCart(CartItem item) {
        FirebaseUser firebaseUser = authService.getCurrentUser();
        String userId = firebaseUser.getUid();

        Map<String, CartItem> newCartMap = populateMap();
        if(newCartMap.containsKey(item.getProductSku())) {
            newCartMap.remove(item.getProductSku());
        }

        userDataRef.child("users").child(userId).child("cartItems").setValue(new ArrayList<>(newCartMap.values()));
    }

    /**
     * This method deletes all items from user's cart.
     */
    public void emptyCart() {
        FirebaseUser firebaseUser = authService.getCurrentUser();
        String userId = firebaseUser.getUid();

        userDataRef.child("users").child(userId).child("cartItems").setValue(new ArrayList<>());
    }

    /**
     * This method updates the quantity of a product that is already in the cart.
     * @param item
     */
    public void updateItemQty(CartItem item) {
        FirebaseUser firebaseUser = authService.getCurrentUser();
        String userId = firebaseUser.getUid();

        Map<String, CartItem> newCartMap = populateMap();
        newCartMap.put(item.getProductSku(), item);

        userDataRef.child("users").child(userId).child("cartItems").setValue(new ArrayList<>(newCartMap.values()));
    }

    /**
     * This method places an order
     * @param transaction
     */
    public void completeTransaction(Transaction transaction, Consumer<Transaction> successCallBack) {
        generateTransactionId(transaction);
        FirebaseUser firebaseUser = authService.getCurrentUser();
        String userId = firebaseUser.getUid();
        Task<DataSnapshot> transactionHistorySnapshot =  userDataRef.child("user").child("transactionHistory").get();
        transactionHistorySnapshot.addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                TransactionHistory transactionHistory = dataSnapshot.getValue(TransactionHistory.class);

                if(transactionHistory == null) {
                    //user has no previous transactions
                    //create a new list and save
                    List<Transaction> transactionList = new ArrayList<>();
                    transactionList.add(transaction);
                    TransactionHistory history = new TransactionHistory();
                    history.setTransactionList(transactionList);
                    userDataRef.child("users").child(userId).child("transactionHistory").setValue(history);
                } else {
                    //user has previous transactions
                    //update the list and push
                    transactionHistory.getTransactionList().add(transaction);
                    userDataRef.child("users").child(userId).child("transactionHistory").setValue(transactionHistory);
                }
                successCallBack.accept(transaction);
                emptyCart();
            }
        });

    }

    private void generateTransactionId(Transaction transaction) {
        transaction.setTransactionId(generateId());
    }
    protected String generateId() {
        StringBuilder idBuilder = new StringBuilder();
        Random rnd = new Random();
        while (idBuilder.length() < 10) { // length of the random string.
            int index = (int) (rnd.nextFloat() * ID_CHARS.length());
            idBuilder.append(ID_CHARS.charAt(index));
        }
        String id = idBuilder.toString();
        return id;

    }
}
