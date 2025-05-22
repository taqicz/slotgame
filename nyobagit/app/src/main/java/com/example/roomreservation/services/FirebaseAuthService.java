package com.example.roomreservation.services;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.roomreservation.R;
import com.example.roomreservation.activities.LoginActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class FirebaseAuthService {
    private static final String TAG = "FirebaseAuthService";
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private Context context;

    public FirebaseAuthService(Context context) {
        this.context = context;
        mAuth = FirebaseAuth.getInstance();
        
        // Configure Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        
        mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
    }

    public Intent getGoogleSignInIntent() {
        return mGoogleSignInClient.getSignInIntent();
    }

    public void firebaseAuthWithGoogle(String idToken, AuthCallback callback) {
        mAuth.signInWithCredential(GoogleAuthProvider.getCredential(idToken, null))
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            callback.onAuthSuccess(user);
                        } else {
                            // If sign in fails
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            callback.onAuthFailure(task.getException());
                        }
                    }
                });
    }

    public void signOut() {
        mAuth.signOut();
        mGoogleSignInClient.signOut();
    }

    public boolean isUserLoggedIn() {
        return mAuth.getCurrentUser() != null;
    }

    public interface AuthCallback {
        void onAuthSuccess(FirebaseUser user);
        void onAuthFailure(Exception exception);
    }
}
