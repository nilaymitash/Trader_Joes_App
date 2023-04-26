package com.trader.joes.model;

import com.trader.joes.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Reference: https://stackoverflow.com/questions/28207252/regex-for-determining-credit-cards-in-android
 */
public class CardPattern {
    private String pattern;
    private int drawableImg;
    private List<CardPattern> allPatterns = new ArrayList<>();

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public int getDrawableImg() {
        return drawableImg;
    }

    public void setDrawableImg(int drawableImg) {
        this.drawableImg = drawableImg;
    }

    public List<CardPattern> getAllPatterns() {
        CardPattern visaPattern = new CardPattern();
        visaPattern.setPattern("^4[0-9]{6,}$");
        visaPattern.setDrawableImg(R.drawable.visa);

        CardPattern mastercardPattern = new CardPattern();
        mastercardPattern.setPattern("^5[1-5][0-9]{5,}$");
        mastercardPattern.setDrawableImg(R.drawable.mastercard);

        CardPattern discoverPattern = new CardPattern();
        discoverPattern.setPattern("^6(?:011|5[0-9]{2})[0-9]{3,}$");
        discoverPattern.setDrawableImg(R.drawable.discover);

        CardPattern amexPattern = new CardPattern();
        amexPattern.setPattern("^4[0-9]{6,}$");
        amexPattern.setDrawableImg(R.drawable.amex);

        CardPattern dinClubPattern = new CardPattern();
        dinClubPattern.setPattern("^3(?:0[0-5]|[68][0-9])[0-9]{4,}$");
        dinClubPattern.setDrawableImg(0);

        CardPattern jcbPattern = new CardPattern();
        jcbPattern.setPattern("^(?:2131|1800|35[0-9]{3})[0-9]{3,}$");
        jcbPattern.setDrawableImg(0);

        this.allPatterns.add(visaPattern);
        this.allPatterns.add(mastercardPattern);
        this.allPatterns.add(discoverPattern);
        this.allPatterns.add(amexPattern);

        return allPatterns;
    }

    public void setAllPatterns(List<CardPattern> allPatterns) {
        this.allPatterns = allPatterns;
    }
}
