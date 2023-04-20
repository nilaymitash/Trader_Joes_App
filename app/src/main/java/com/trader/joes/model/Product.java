package com.trader.joes.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Product implements Serializable {

    private String sku;
    private String productName;
    private List<String> productCharacteristics = new ArrayList<>();
    private String price;
    private String imgURL;
    private List<Review> reviews;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public List<String> getProductCharacteristics() {
        return productCharacteristics;
    }

    public void setProductCharacteristics(List<String> productCharacteristics) {
        this.productCharacteristics = productCharacteristics;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public float getRating() {
        if(reviews != null) {
            float totalRating = 0;
            for(Review review : this.reviews) {
                totalRating += review.getRating();
            }
            return totalRating/this.reviews.size();
        }
        return 0;
    }

    public int getNumOfRatings() {
        if(reviews != null)
            return this.reviews.size();
        else
            return 0;
    }
}
