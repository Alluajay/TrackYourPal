package com.example.allu.trackyourpal.UI.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.allu.trackyourpal.Adapter.AdapterFriendRequests;
import com.example.allu.trackyourpal.POJO.FriendUUid;
import com.example.allu.trackyourpal.POJO.User;
import com.example.allu.trackyourpal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.allu.trackyourpal.Utils.Attributes.Fire_Friends;
import static com.example.allu.trackyourpal.Utils.Attributes.Fire_Users;


public class FragmentFriends extends Fragment  {

    String TAG = FragmentFriends.class.getSimpleName();
    Context context;
    DatabaseReference reference;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    RecyclerView recyFriends;

    ArrayList<FriendUUid> friendsList;
    ArrayList<User> userArrayList;
    AdapterFriendRequests adapter_friend_requests;


    public FragmentFriends() {
        // Required empty public constructor
    }


    public static FragmentFriends newInstance(String param1, String param2) {
        FragmentFriends fragment = new FragmentFriends();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reference = FirebaseDatabase.getInstance().getReference();
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        friendsList = new ArrayList<>();
        userArrayList = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.e("friends","init");
        View v = inflater.inflate(R.layout.fragment_fragment__friends, container, false);
        context = v.getContext();
        recyFriends = (RecyclerView)v.findViewById(R.id.recy_friend);
        recyFriends.setItemAnimator(new DefaultItemAnimator());
        recyFriends.setHasFixedSize(false);

        recyFriends.setLayoutManager(new GridLayoutManager(context,1));
        friendsList = new ArrayList<>();
        userArrayList = new ArrayList<>();
        LoadContent1();
        return v;
    }

    void LoadContent1(){
        reference.child(Fire_Users).child(mAuth.getCurrentUser().getUid()).child(Fire_Friends).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG,"freinds found"+dataSnapshot.toString());
                friendsList = new ArrayList<FriendUUid>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    FriendUUid uUid = new FriendUUid();
                    if(snapshot.getValue().equals("req")){
                        //setUidList(snapshot.getKey(),1);
                        uUid = new FriendUUid(snapshot.getKey(),1);
                    }else if(snapshot.getValue().equals(true)){
                       // setUidList(snapshot.getKey(),0);
                        uUid = new FriendUUid(snapshot.getKey(),0);
                    }
                    friendsList.add(uUid);
                }
                findUserFromUuid();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void findUserFromUuid(){
        userArrayList = new ArrayList<>();
        for (final FriendUUid uUid : friendsList){
            reference.child(Fire_Users).child(uUid.UUid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    user.UID = uUid.UUid;
                    user.request = uUid.req;
                    userArrayList.add(user);
                    Log.e(TAG,"user added" +uUid.req+" "+userArrayList.toString());
                    setList();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    void setList(){
        adapter_friend_requests = new AdapterFriendRequests(userArrayList,context,database,mAuth);
        recyFriends.setAdapter(adapter_friend_requests);
    }

    @Override
    public void onPause() {
        super.onPause();
        friendsList = new ArrayList<>();
    }

    @Override
    public void onResume() {
        super.onResume();
        LoadContent1();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        friendsList = new ArrayList<>();
    }

}
