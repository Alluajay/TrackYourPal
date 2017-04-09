package com.example.allu.trackyourpal.UI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.allu.trackyourpal.POJO.Message;
import com.example.allu.trackyourpal.R;
import com.example.allu.trackyourpal.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.allu.trackyourpal.User_Utils.Attributes.Fire_Discussion;
import static com.example.allu.trackyourpal.User_Utils.Attributes.Fire_Lat;
import static com.example.allu.trackyourpal.User_Utils.Attributes.Fire_Long;
import static com.example.allu.trackyourpal.User_Utils.Attributes.Fire_Tour;

public class TourActivity extends AppCompatActivity implements View.OnClickListener {

    String TAG = "TourActivity";
    private DatabaseReference mDatabase;
    private FirebaseAuth auth;

    Utils utils;

    EditText Edit_msg;

    long lenght;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour);

        utils = new Utils(this);

        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(auth.getCurrentUser().getUid());

        Edit_msg = (EditText)findViewById(R.id.edit_msg);

        mDatabase.child(Fire_Tour).child(Fire_Lat).setValue("123123123");
        mDatabase.child(Fire_Tour).child(Fire_Long).setValue("987987987");

        mDatabase.child(Fire_Tour).child(Fire_Discussion).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lenght = dataSnapshot.getChildrenCount();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Message message = snapshot.getValue(Message.class);
                    Log.e(TAG,message.Message);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void AddMsg(){
        String msg = Edit_msg.getText().toString();
        Message message = new Message(auth.getCurrentUser().getUid(),msg,utils.usernameFromEmail(auth.getCurrentUser().getEmail()));
        mDatabase.child(Fire_Tour).child(Fire_Discussion).child(lenght+"").setValue(message);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_addmsg:
                AddMsg();
                break;
        }
    }
}
