package com.example.allu.trackyourpal.UI.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.allu.trackyourpal.Adapter.Adapter_friend_requests;
import com.example.allu.trackyourpal.POJO.User;
import com.example.allu.trackyourpal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.allu.trackyourpal.User_Utils.Attributes.Fire_Friends;
import static com.example.allu.trackyourpal.User_Utils.Attributes.Fire_Users;


public class Fragment_findFriends extends Fragment {

    String TAG = Fragment_Friends.class.getSimpleName();
    FirebaseDatabase mDatabase;
    FirebaseAuth auth;
    ArrayList<String> UidList;
    ArrayList<User> userArrayList;
    Adapter_friend_requests adapter_friend_requests;


    RecyclerView recyclerView_frnd_requests;

     public Fragment_findFriends() {
        // Required empty public constructor
    }

    public static Fragment_findFriends newInstance(String param1, String param2) {
        Fragment_findFriends fragment = new Fragment_findFriends();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UidList = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        userArrayList = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fragment_find_friends, container, false);
        recyclerView_frnd_requests = (RecyclerView)v.findViewById(R.id.recy_friends);
        recyclerView_frnd_requests.setItemAnimator(new DefaultItemAnimator());
        recyclerView_frnd_requests.setLayoutManager(new GridLayoutManager(getActivity().getApplicationContext(),1));
        recyclerView_frnd_requests.setHasFixedSize(false);

        LoadContent();
        return v;
    }

    void LoadContent(){
        mDatabase.getReference().child(Fire_Users).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userArrayList = new ArrayList<User>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    user.UID = snapshot.getKey();
                    user.request = 2;
                    userArrayList.add(user);
                    Log.e(TAG,"added user"+user.Username+" req:"+user.request);
                    adapter_friend_requests = new Adapter_friend_requests(userArrayList,getActivity().getApplicationContext(),mDatabase,auth);
                    recyclerView_frnd_requests.setAdapter(adapter_friend_requests);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void setUidList(final String uid, final int req){
        mDatabase.getReference().child(Fire_Users).child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                user.UID = uid;
                user.request = req;
                userArrayList.add(user);
                Log.e(TAG,"added user"+user.Username+" req:"+user.request);
                adapter_friend_requests = new Adapter_friend_requests(userArrayList,getActivity().getApplicationContext(),mDatabase,auth);
                recyclerView_frnd_requests.setAdapter(adapter_friend_requests);
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
