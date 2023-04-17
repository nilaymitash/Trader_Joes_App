package com.trader.joes.model;

import java.util.List;

public class User {

    private String uID;
    private Cart cart;
    private List<PaymentCard> savedCards;

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public List<PaymentCard> getSavedCards() {
        return savedCards;
    }

    public void setSavedCards(List<PaymentCard> savedCards) {
        this.savedCards = savedCards;
    }
}
