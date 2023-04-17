package com.trader.joes.model;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String uID;
    private List<CartItem> cartItems;
    private List<PaymentCard> savedCards;

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public void addItemToCart(CartItem item) {
        if (this.cartItems == null) {
            this.cartItems = new ArrayList<>();
        }
        cartItems.add(item);
    }

    public List<PaymentCard> getSavedCards() {
        return savedCards;
    }

    public void setSavedCards(List<PaymentCard> savedCards) {
        this.savedCards = savedCards;
    }
}
