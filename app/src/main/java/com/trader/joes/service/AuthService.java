package com.trader.joes.service;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.function.Consumer;

/**
 * AuthService is responsible for handling all
 * Authentication and Authorization calls to Firebase.
 */
public class AuthService {

    private final FirebaseAuth mAuth;

    public AuthService() {
        this.mAuth = FirebaseAuth.getInstance();
    }

    public boolean isUserLoggedIn() {
        return mAuth.getCurrentUser() != null;
    }

    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    /**
     * This method creates an account using email and password.
     * Requires the caller to provide 1 success callback function and 1 failure callback function
     * @param email
     * @param password
     * @param activity
     * @param successCallback
     * @param failureCallback
     */
    public void createAccount(String email, String password, Activity activity, Consumer<Void> successCallback, Consumer<String> failureCallback) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            //Call the success callback function provided by the caller
                            successCallback.accept(null);
                        } else {
                            //Call the failure callback function provided by the caller
                            failureCallback.accept(task.getException().getMessage());
                        }
                    }
                });
    }

    /**
     * This method is used to log in using email and password.
     * Requires the caller to provide 1 success callback function and 1 failure callback function
     * @param email
     * @param password
     * @param activity
     * @param successCallback
     * @param failureCallback
     */
    public void signIn(String email, String password, Activity activity, Consumer successCallback, Consumer failureCallback) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //Call the success callback function provided by the caller
                            successCallback.accept(null);
                        } else {
                            //Call the failure callback function provided by the caller
                            failureCallback.accept(task.getException().getMessage());
                        }
                    }
                });
    }

    public void updateUserProfile(String displayName, String email, Consumer<Void> successCallback) {
        FirebaseUser user = getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .build();

        updateDisplayName(user, profileUpdates, successCallback);

        updateEmail(email, successCallback, user);
    }

    private void updateEmail(String email, Consumer<Void> successCallback, FirebaseUser user) {
        user.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("Auth Service", "User email address updated.");
                    successCallback.accept(null);
                } else {
                    Log.d("Auth Service", "User email address update failed.");
                }
            }
        });
    }

    private void updateDisplayName(FirebaseUser user, UserProfileChangeRequest profileUpdates, Consumer<Void> successCallback) {
        user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("Auth Service", "User profile updated.");
                    successCallback.accept(null);
                }
            }
        });
    }

    /**
     * This method is used to log a user out.
     */
    public void signOut() {
        mAuth.signOut();
    }
}
