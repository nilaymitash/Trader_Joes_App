package com.trader.joes.service;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.trader.joes.activity.LoginActivity;
import com.trader.joes.activity.ProductListActivity;
import com.trader.joes.activity.SignupActivity;

import java.util.function.Consumer;
import java.util.function.Function;

public class AuthService {

    private final FirebaseAuth mAuth;

    public AuthService() {
        this.mAuth = FirebaseAuth.getInstance();
    }

    public boolean isUserLoggedIn() {
        return mAuth.getCurrentUser() != null;
    }

    public FirebaseUser getCurrentuser() {
        return mAuth.getCurrentUser();
    }

    public void createAccount(String email, String password, Activity activity, Consumer successCallback, Consumer failureCallback) {
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
