package com.trader.joes.service;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * This service contains all the common utilities functions
 */
public class UtilityService {

    /**
     * This function hides the keyboard.
     * Intended use is to be called on click of any button that is part of a form entry
     * @param activity
     * @param layout
     */
    public static void hideKeyboard(Activity activity, View layout) {
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(layout.getWindowToken(), 0); //hide numeric keyboard
    }
}
