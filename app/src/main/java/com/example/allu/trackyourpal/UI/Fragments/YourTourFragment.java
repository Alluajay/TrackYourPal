package com.example.allu.trackyourpal.UI.Fragments;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.example.allu.trackyourpal.Adapter.Adapter_Message;
import com.example.allu.trackyourpal.POJO.Message;
import com.example.allu.trackyourpal.R;
import com.example.allu.trackyourpal.UI.TourViewActivity;
import com.example.allu.trackyourpal.Utils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.allu.trackyourpal.User_Utils.Attributes.Fire_Discussion;
import static com.example.allu.trackyourpal.User_Utils.Attributes.Fire_Lat;
import static com.example.allu.trackyourpal.User_Utils.Attributes.Fire_Long;
import static com.example.allu.trackyourpal.User_Utils.Attributes.Fire_Online;
import static com.example.allu.trackyourpal.User_Utils.Attributes.Fire_Tour;
import static com.example.allu.trackyourpal.User_Utils.Attributes.Fire_Users;


public class YourTourFragment extends Fragment implements OnMapReadyCallback{
    String TAG = YourTourFragment.class.getSimpleName();
    Utils utils;
    MapFragment mapFragment;

    String Uid;

    DatabaseReference mDatabase;
    FirebaseAuth mAuth;

    double lat,longi;
    LatLng latLng;

    GoogleMap googleMap;

    EditText Edit_message;

    RecyclerView Recy_messages;
    ArrayList<Message> messages;
    Adapter_Message adapter_message;
    long lenght;

    FloatingActionButton Fab_sendmessage;
    public YourTourFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        utils = new Utils(this.getActivity().getApplicationContext());

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Fire_Users).child(Uid);
        mDatabase.child(Fire_Online).setValue(true);
        messages = new ArrayList<>();
        adapter_message = new Adapter_Message(this.getActivity().getApplicationContext(),messages,mAuth);
        Uid = "XnTx7mWXNCaeGRQunGAL3HUV25X2";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.e("tour","init");
        View v = inflater.inflate(R.layout.fragment_your_tour, container, false);
        Edit_message = (EditText)v.findViewById(R.id.edit_msg);
        mapFragment = (MapFragment) this.getActivity().getFragmentManager().findFragmentById(R.id.map);

        Recy_messages = (RecyclerView)v.findViewById(R.id.recy_message);
        Fab_sendmessage = (FloatingActionButton)v.findViewById(R.id.fab_sendmsg);
        Recy_messages.setHasFixedSize(false);
        Recy_messages.setItemAnimator(new DefaultItemAnimator());
        Recy_messages.setLayoutManager(new GridLayoutManager(this.getActivity().getApplicationContext(),1));
        Recy_messages.setAdapter(adapter_message);
        loadContent();
        return v;
    }

    void loadContent(){

        mapFragment.getMapAsync(this);

        mDatabase.child(Fire_Tour).child(Fire_Discussion).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lenght = dataSnapshot.getChildrenCount();
                messages = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Message message = snapshot.getValue(Message.class);
                    Log.e(TAG,message.Message);
                    messages.add(message);
                }
                adapter_message = new Adapter_Message(getActivity().getApplicationContext(),messages,mAuth);
                Recy_messages.setAdapter(adapter_message);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Fab_sendmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddMsg();
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
        googleMap.clear();
    }

    void getLatLong(){
        lat = 0;
        longi = 0;
        setmarker();
        mDatabase.child(Fire_Tour).child(Fire_Lat).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lat = Double.parseDouble(dataSnapshot.getValue().toString());
                setmarker();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabase.child(Fire_Tour).child(Fire_Long).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                longi = Double.parseDouble(dataSnapshot.getValue().toString());
                setmarker();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void setmarker(){
        Log.e(TAG,"markerAdded");
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat, longi))
                .title("Marker"));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        getLatLong();
    }

    void AddMsg(){
        String msg = Edit_message.getText().toString();
        Message message = new Message(mAuth.getCurrentUser().getUid(),msg,utils.usernameFromEmail(mAuth.getCurrentUser().getEmail()));
        mDatabase.child(Fire_Tour).child(Fire_Discussion).child(lenght+"").setValue(message);
        Edit_message.setText("");
    }


}
