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

import com.example.allu.trackyourpal.POJO.User;
import com.example.allu.trackyourpal.R;
import com.example.allu.trackyourpal.Utils.Utils;
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

import static com.example.allu.trackyourpal.Utils.Attributes.Fire_Friends;
import static com.example.allu.trackyourpal.Utils.Attributes.Fire_Users;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    String TAG = LoginActivity.class.getSimpleName();
    Utils utils;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    TextView loginTitle;
    Typeface loginTypeface;

    Button loginButton,btn_signup_default;
    EditText loginEmail, loginPassword;
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

        loginTitle = (TextView)findViewById(R.id.txt_login_title);
        loginTypeface = Typeface.createFromAsset(getAssets(),"fonts/Gorditas-Regular.ttf");
        loginTitle.setTypeface(loginTypeface);

        loginButton = (Button)findViewById(R.id.btn_login);
        loginEmail = (EditText)findViewById(R.id.edit_email);
        loginPassword = (EditText)findViewById(R.id.edit_password);
        btn_signup_default = (Button)findViewById(R.id.btn_signup_default);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        btn_signup_default.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               utils.Goto(SignupActivity.class);
            }
        });

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


    void login(){
        String Emailid,Password;
        Emailid = loginEmail.getText().toString();
        Password = loginPassword.getText().toString();
        if(Emailid.isEmpty() || Emailid.trim().equals("") || Password.isEmpty() || Password.trim().equals("")){
            utils.toast(getString(R.string.enteruserpass));
            return;
        }else{
            utils.showDialog();
            mAuth.signInWithEmailAndPassword(Emailid,Password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    utils.closeDialog();
                    Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "signInWithEmail", task.getException());
                        utils.toast(getString(R.string.autherror));
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
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);

        mDatabase.child(Fire_Users).child(userId).setValue(user);
        mDatabase.child(Fire_Friends).child(userId);

    }


    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            firebaseAuthWithGoogle(acct);
        } else {
            utils.toast(getString(R.string.errorinsigin));
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
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            utils.toast(getString(R.string.autherror)+mAuth.getCurrentUser().getUid());
                        }else{
                            utils.toast(getString(R.string.loginsuccess));
                            writeNewUser(mAuth.getCurrentUser().getUid(),acct.getDisplayName(),mAuth.getCurrentUser().getEmail());
                            utils.Goto(HomeActivity.class);
                        }
                    }
                });
    }

}
