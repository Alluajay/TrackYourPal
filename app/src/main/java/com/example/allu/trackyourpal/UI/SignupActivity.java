package com.example.allu.trackyourpal.UI;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.allu.trackyourpal.POJO.User;
import com.example.allu.trackyourpal.R;
import com.example.allu.trackyourpal.Utils.Attributes;
import com.example.allu.trackyourpal.Utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.allu.trackyourpal.Utils.Attributes.Fire_Friends;

public class SignupActivity extends AppCompatActivity {

    String TAG = SignupActivity.class.getSimpleName();
    Utils utils;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;

    EditText editEmailid, editPass;
    Button btnSignup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        utils = new Utils(this);



        editEmailid = (EditText)findViewById(R.id.edit_email);
        editPass = (EditText)findViewById(R.id.edit_password);

        btnSignup = (Button)findViewById(R.id.btn_signup);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();
            }
        });

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

    void signup(){
        String Emailid = editEmailid.getText().toString().trim();
        String Pass = editPass.getText().toString().trim();
        if(Emailid.isEmpty() || Emailid.equals("") || Pass.isEmpty() || Pass.equals("")){
            utils.toast(getString(R.string.enterallfeilds));
            return;
        }

        if (Pass.length() < 6) {
            Toast.makeText(getApplicationContext(), getString(R.string.passtooshort), Toast.LENGTH_SHORT).show();
            return;
        }
        utils.showDialog();
        mAuth.createUserWithEmailAndPassword(Emailid, Pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        utils.closeDialog();
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.e(TAG,task.toString());
                            utils.toast(getString(R.string.autherror)+task.toString());
                        }else {
                            onAuthSuccess(task.getResult().getUser());
                            utils.toast(getString(R.string.usercreationsucc));
                            utils.Goto(LoginActivity.class);
                        }
                    }
                });
    }

    private void onAuthSuccess(FirebaseUser user) {
        String username = usernameFromEmail(user.getEmail());
        writeNewUser(user.getUid(), username, user.getEmail());
    }
    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }


    private void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);
        mDatabase.child(Attributes.Fire_Users).child(userId).setValue(user);
        mDatabase.child(Fire_Friends).child(userId);
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
