package com.example.allu.trackyourpal.UI;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.allu.trackyourpal.R;
import com.example.allu.trackyourpal.User_Utils.User_utils;
import com.example.allu.trackyourpal.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    String TAG = "LoginActivity";
    Utils utils;
    User_utils user_utils;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    TextView LoginTitle;
    Typeface Login_typeface;

    Button Login_button,btn_signup_default;
    EditText Login_email,Login_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();


        utils = new Utils(this);

        LoginTitle = (TextView)findViewById(R.id.txt_login_title);
        Login_typeface = Typeface.createFromAsset(getAssets(),"fonts/Gorditas-Regular.ttf");
        LoginTitle.setTypeface(Login_typeface);

        Login_button = (Button)findViewById(R.id.btn_login);
        Login_email = (EditText)findViewById(R.id.edit_email);
        Login_password = (EditText)findViewById(R.id.edit_password);
        btn_signup_default = (Button)findViewById(R.id.btn_signup_default);

        Login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login();
            }
        });
        btn_signup_default.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG,"clicked");
                //utils.Goto(MainActivity.class);
               utils.Goto(SignupActivity.class);
            }
        });

        user_utils = new User_utils(this);
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            utils.Goto(HomeActivity.class);
            finish();
        }
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


    void Login(){
        String Emailid,Password;
        Emailid = Login_email.getText().toString();
        Password = Login_password.getText().toString();
        if(Emailid.isEmpty() || Emailid.trim().equals("") || Password.isEmpty() || Password.trim().equals("")){
            utils.Toast("Enter the Username and Password..");
            return;
        }else{
            mAuth.signInWithEmailAndPassword(Emailid,Password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "signInWithEmail", task.getException());
                        utils.Toast("Authentication Failed");
                    }else{
                        utils.Goto(HomeActivity.class);
                    }

                }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}
