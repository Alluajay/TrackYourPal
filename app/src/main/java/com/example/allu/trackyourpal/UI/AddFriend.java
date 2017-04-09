package com.example.allu.trackyourpal.UI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.allu.trackyourpal.POJO.User;
import com.example.allu.trackyourpal.R;
import com.example.allu.trackyourpal.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddFriend extends AppCompatActivity {
    String TAG = "AddFriendActivity";
    Utils utils;

    private DatabaseReference mDatabase;
    private FirebaseAuth auth;

    ArrayList<User> userArrayList;
    ArrayList<String> username;
    ListView List_friends;

    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        utils = new Utils(this);
        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();



        List_friends = (ListView)findViewById(R.id.list_friends);


        mDatabase.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userArrayList = new ArrayList<>();
                username = new ArrayList<>();
                Log.e(TAG,dataSnapshot.toString());
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Log.e(TAG,postSnapshot.toString());
                    User user = postSnapshot.getValue(User.class);
                    user.UID = postSnapshot.getKey();
                    userArrayList.add(user);
                    username.add(user.Username);
                    Log.e(TAG,"user added "+user.Emailid);
                }
                String[] names = new String[userArrayList.size()];
                for(int i = 0 ;i<userArrayList.size();i++){
                    names[i] = userArrayList.get(i).Username;
                }
                ArrayAdapter adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_2, names);
                List_friends.setAdapter(adapter);
                AdapterClick();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                utils.Toast("Error in fetching the data");
            }
        });

    }

    void AdapterClick(){
        List_friends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e(TAG,username.get(i));
                AddFriendtoFirebase(userArrayList.get(i));
            }
        });
    }

    void AddFriendtoFirebase(User user){
        DatabaseReference reference = mDatabase.child("users").child(auth.getCurrentUser().getUid());
        reference.child("friends").child(user.UID).setValue(true);
    }

    private void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);

        mDatabase.child("users").child(userId).setValue(user);

        //ArrayList<String> userNames = new ArrayList<>();
        //userNames.add(name);
        //mDatabase.child("usernamelist").setValue(userNames);
    }

    void PushData(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("UID");

        myRef.setValue("Hello, World!");
    }
}
