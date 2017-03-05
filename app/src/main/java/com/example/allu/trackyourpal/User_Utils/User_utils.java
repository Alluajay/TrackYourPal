package com.example.allu.trackyourpal.User_Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by allu on 3/6/17.
 */

public class User_utils {
    String TAG = "User_utils";
    Context mContext;
    private static FirebaseAuth mAuth;
    private static FirebaseAuth.AuthStateListener mAuthListener;

    public User_utils(Context context){
        mContext = context;
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                }else{
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    public static FirebaseAuth getmAuth() {
        return mAuth;
    }

    public static void setmAuth(FirebaseAuth mAuth) {
        User_utils.mAuth = mAuth;
    }

    public static FirebaseAuth.AuthStateListener getmAuthListener() {
        return mAuthListener;
    }

    public static void setmAuthListener(FirebaseAuth.AuthStateListener mAuthListener) {
        User_utils.mAuthListener = mAuthListener;
    }


}
