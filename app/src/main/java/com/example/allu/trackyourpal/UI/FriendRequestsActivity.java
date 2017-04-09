package com.example.allu.trackyourpal.UI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.allu.trackyourpal.Adapter.Adapter_friend_requests;
import com.example.allu.trackyourpal.POJO.User;
import com.example.allu.trackyourpal.R;
import com.example.allu.trackyourpal.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.allu.trackyourpal.User_Utils.Attributes.Fire_Friends;
import static com.example.allu.trackyourpal.User_Utils.Attributes.Fire_Username;
import static com.example.allu.trackyourpal.User_Utils.Attributes.Fire_Users;

public class FriendRequestsActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseDatabase database;
    ArrayList<String> UidList;
    ArrayList<User> userArrayList;
    Adapter_friend_requests adapter_friend_requests;

    String TAG = FriendRequestsActivity.class.getSimpleName();
    Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_requests);
        utils = new Utils(this);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();


    }

    void LoadContent(){
        database.getReference().child(Fire_Users).child(Fire_Friends).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UidList = new ArrayList<String>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if(snapshot.getValue().equals("req")){
                        UidList.add(snapshot.getKey());
                    }
                }
                setUidList();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void setUidList(){
        userArrayList = new ArrayList<>();
        for (final String uid : UidList){
            database.getReference().child(Fire_Users).child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    user.UID = uid;
                    user.request = 1;
                    userArrayList.add(user);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        for (User user : userArrayList){
            Log.e(TAG,user.UID+" "+user.Username);
        }
    }
}
