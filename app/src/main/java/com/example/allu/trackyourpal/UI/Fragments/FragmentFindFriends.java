package com.example.allu.trackyourpal.UI.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.allu.trackyourpal.Adapter.AdapterFriendRequests;
import com.example.allu.trackyourpal.POJO.User;
import com.example.allu.trackyourpal.R;
import com.example.allu.trackyourpal.Utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static com.example.allu.trackyourpal.Utils.Attributes.Fire_Username;
import static com.example.allu.trackyourpal.Utils.Attributes.Fire_Users;


public class FragmentFindFriends extends Fragment {

    String TAG = FragmentFindFriends.class.getSimpleName();
    Context context;
    FirebaseDatabase mDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    ArrayList<String> uidList;
    ArrayList<User> userArrayList;
    AdapterFriendRequests adapter_friend_requests;

    FloatingActionButton fabFindfriends;
    EditText editQuery;

    Utils utils;


    RecyclerView recyclerView_frnd_requests;

     public FragmentFindFriends() {
        // Required empty public constructor
     }

    public static FragmentFindFriends newInstance(String param1, String param2) {
        FragmentFindFriends fragment = new FragmentFindFriends();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        utils = new Utils(context);
        uidList = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance();
        databaseReference = mDatabase.getReference();
        auth = FirebaseAuth.getInstance();
        userArrayList = new ArrayList<>();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fragment_find_friends, container, false);
        recyclerView_frnd_requests = (RecyclerView)v.findViewById(R.id.recy_friends);
        recyclerView_frnd_requests.setItemAnimator(new DefaultItemAnimator());
        recyclerView_frnd_requests.setLayoutManager(new GridLayoutManager(getActivity().getApplicationContext(),1));
        recyclerView_frnd_requests.setHasFixedSize(false);

        editQuery = (EditText)v.findViewById(R.id.edit_findfriend);
        fabFindfriends = (FloatingActionButton)v.findViewById(R.id.fab_findfriend);
        fabFindfriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findFriend();
            }
        });
        loadContent();
        return v;
    }

    void findFriend(){
        Log.e(TAG,"search init");
        String query_string = editQuery.getText().toString();
        if(utils.isEmptyString(query_string)){
            return;
        }
        Query query = databaseReference.orderByChild(Fire_Username).equalTo("allu");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG,"search query :"+dataSnapshot.toString());
                if (dataSnapshot.exists()) {
                    userArrayList = new ArrayList<User>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Log.e(TAG,"search query :"+snapshot.toString());
                        if(snapshot.getKey().equals(auth.getCurrentUser().getUid())){
                            continue;
                        }
                        User user = snapshot.getValue(User.class);
                        user.UID = snapshot.getKey();
                        user.request = 2;
                        userArrayList.add(user);
                        Log.e(TAG,"added user"+user.Username+" req:"+user.request);
                        Set<User> users = new HashSet<User>(userArrayList);
                        adapter_friend_requests = new AdapterFriendRequests(users,getActivity().getApplicationContext(),mDatabase,auth);
                        recyclerView_frnd_requests.setAdapter(adapter_friend_requests);

                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG,"search cancelled");
            }
        });
    }

    void loadContent(){
        mDatabase.getReference().child(Fire_Users).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userArrayList = new ArrayList<User>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if(snapshot.getKey().equals(auth.getCurrentUser().getUid())){
                        continue;
                    }
                    User user = snapshot.getValue(User.class);
                    user.UID = snapshot.getKey();
                    user.request = 2;
                    userArrayList.add(user);
                    Log.e(TAG,"search added user"+user.Username+" req:"+user.request);
                    Set<User> users = new HashSet<User>(userArrayList);
                    adapter_friend_requests = new AdapterFriendRequests(users,context,mDatabase,auth);
                    recyclerView_frnd_requests.setAdapter(adapter_friend_requests);
                }

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
