package com.trader.joes.service;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.function.Consumer;

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

    public void createAccount(String email, String password, Activity activity, Consumer<Void> successCallback, Consumer<String> failureCallback) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            successCallback.accept(null);
                        } else {
                            failureCallback.accept(task.getException().getMessage());
                        }
                    }
                });
    }

    public void signIn(String email, String password, Activity activity, Consumer successCallback, Consumer failureCallback) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            successCallback.accept(null);
                        } else {
                            failureCallback.accept(task.getException().getMessage());
                        }
                    }
                });
    }

    public void signOut() {
        mAuth.signOut();
    }
}
