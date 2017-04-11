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
import android.widget.Toast;

import com.example.allu.trackyourpal.POJO.User;
import com.example.allu.trackyourpal.R;
import com.example.allu.trackyourpal.User_Utils.User_utils;
import com.example.allu.trackyourpal.Utils;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.allu.trackyourpal.User_Utils.Attributes.Fire_Friends;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    String TAG = "LoginActivity";
    Utils utils;
    User_utils user_utils;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    TextView LoginTitle;
    Typeface Login_typeface;

    Button Login_button,btn_signup_default;
    EditText Login_email,Login_password;
    SignInButton signInButton;

    int RC_SIGN_IN = 100;
    GoogleSignInOptions googleSignInOptions;
    GoogleApiClient mGoogleApiClient;

    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();


        utils = new Utils(this);

        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.google_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

        signInButton = (SignInButton)findViewById(R.id.btn_signup_google);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();

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
            utils.ShowDialog();
            mAuth.signInWithEmailAndPassword(Emailid,Password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    utils.CloseDialog();
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void signIn() {
        Log.e(TAG,"signin init");
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
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

        mDatabase.child("users").child(userId).setValue(user);
        mDatabase.child(Fire_Friends).child(userId);

    }


    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Log.e(TAG,"signed in");
            firebaseAuthWithGoogle(acct);
        } else {
            utils.Toast("error in sigining up");
        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        Log.e(TAG, "firebaseAuthWithGoogle:" + acct.getId()+" "+acct.getIdToken());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            utils.Toast("authentification failed"+mAuth.getCurrentUser().getUid());
                        }else{
                            utils.Toast("login successfull");
                            writeNewUser(mAuth.getCurrentUser().getUid(),acct.getDisplayName(),mAuth.getCurrentUser().getEmail());
                            utils.Goto(HomeActivity.class);
                        }
                        // ...
                    }
                });
    }

}
