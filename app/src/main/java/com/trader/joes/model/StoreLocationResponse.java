package com.trader.joes.model;

import java.util.List;

public class StoreLocationResponse {
    private List<Store> collection;

    public List<Store> getCollection() {
        return collection;
    }

    public void setCollection(List<Store> collection) {
        this.collection = collection;
    }
}
