package com.example.allu.trackyourpal.UI.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.allu.trackyourpal.Adapter.Adapter_friend_requests;
import com.example.allu.trackyourpal.POJO.User;
import com.example.allu.trackyourpal.R;
import com.example.allu.trackyourpal.UI.TourViewActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.allu.trackyourpal.User_Utils.Attributes.Fire_Friends;
import static com.example.allu.trackyourpal.User_Utils.Attributes.Fire_Username;
import static com.example.allu.trackyourpal.User_Utils.Attributes.Fire_Users;
import static com.example.allu.trackyourpal.User_Utils.Attributes.Intent_uid;


public class Fragment_Friends extends Fragment {

    String TAG = Fragment_Friends.class.getSimpleName();
    DatabaseReference reference;
    FirebaseAuth mAuth;
    ListView List_Friends;

    ArrayList<String> FriendsUidList;
    ArrayList<String> UidList;
    ArrayList<User> userArrayList;
    Adapter_friend_requests adapter_friend_requests;
    String[] Names;


    public Fragment_Friends() {
        // Required empty public constructor
    }


    public static Fragment_Friends newInstance(String param1, String param2) {
        Fragment_Friends fragment = new Fragment_Friends();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reference = FirebaseDatabase.getInstance().getReference();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.e("friends","init");
        View v = inflater.inflate(R.layout.fragment_fragment__friends, container, false);
        List_Friends = (ListView)v.findViewById(R.id.list_friends);
        LoadContent1();
        return v;
    }

    void LoadContent1(){
        reference.child(Fire_Users).child(Fire_Friends).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UidList = new ArrayList<String>();
                userArrayList = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if(snapshot.getValue().equals("req")){
                        setUidList(snapshot.getKey(),1);
                    }else if(snapshot.getValue().equals(true)){
                        setUidList(snapshot.getKey(),0);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void setUidList(final String uid, int req){
        reference.child(Fire_Users).child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                user.UID = uid;
                userArrayList.add(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
