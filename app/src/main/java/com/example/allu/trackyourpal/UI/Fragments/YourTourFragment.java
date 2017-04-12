package com.example.allu.trackyourpal.UI.Fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import com.example.allu.trackyourpal.Adapter.AdapterMessage;
import com.example.allu.trackyourpal.GPS.GPSTracker;
import com.example.allu.trackyourpal.POJO.Message;
import com.example.allu.trackyourpal.R;
import com.example.allu.trackyourpal.Utils.Utils;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.allu.trackyourpal.Utils.Attributes.Fire_Discussion;
import static com.example.allu.trackyourpal.Utils.Attributes.Fire_Lat;
import static com.example.allu.trackyourpal.Utils.Attributes.Fire_Long;
import static com.example.allu.trackyourpal.Utils.Attributes.Fire_Online;
import static com.example.allu.trackyourpal.Utils.Attributes.Fire_Tour;
import static com.example.allu.trackyourpal.Utils.Attributes.Fire_Users;


public class YourTourFragment extends Fragment implements OnMapReadyCallback{
    String TAG = YourTourFragment.class.getSimpleName();
    Utils utils;

    MapView mMapView;
    Context context;
    Marker marker;

    String Uid;

    DatabaseReference mDatabase;
    FirebaseAuth mAuth;

    double lat, longi;
    LatLng latLng;

    GoogleMap googleMap;

    EditText Edit_message;

    RecyclerView Recy_messages;
    ArrayList<Message> messages;
    AdapterMessage adapter_message;
    long lenght;

    FloatingActionButton Fab_sendmessage;



    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG,"received");
            updateLatLong(intent);
        }
    };
    Intent intent;

    public YourTourFragment() {
        // Required empty public constructor
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.e("tour", "init");
        View v = inflater.inflate(R.layout.fragment_your_tour, container, false);
        context = v.getContext();

        intent = new Intent(context, GPSTracker.class);

        Edit_message = (EditText)v.findViewById(R.id.edit_msg);


        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(this);


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


        utils = new Utils(this.getActivity().getApplicationContext());

        mAuth = FirebaseAuth.getInstance();
        Uid = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Fire_Users).child(Uid);
        mDatabase.child(Fire_Online).setValue(true);
        messages = new ArrayList<>();
        adapter_message = new AdapterMessage(this.getActivity().getApplicationContext(),messages,mAuth);

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
                adapter_message = new AdapterMessage(getActivity().getApplicationContext(),messages,mAuth);
                Recy_messages.setAdapter(adapter_message);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        Fab_sendmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMsg();
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        context.startService(intent);
        context.registerReceiver(broadcastReceiver,new IntentFilter(GPSTracker.Broadcast_Action));

    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
        context.unregisterReceiver(broadcastReceiver);
        context.stopService(intent);
    }



    void getLatLong(){
        lat = 0;
        longi = 0;
        setmarker();
        mDatabase.child(Fire_Tour).child(Fire_Lat).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null){
                    lat = Double.parseDouble(dataSnapshot.getValue().toString());
                    setmarker();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabase.child(Fire_Tour).child(Fire_Long).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null){
                    longi = Double.parseDouble(dataSnapshot.getValue().toString());
                    setmarker();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void setmarker(){
        Log.e(TAG,"markerAdded");
        mDatabase.child(Fire_Tour).child(Fire_Lat).setValue(lat);
        mDatabase.child(Fire_Tour).child(Fire_Long).setValue(longi);
        marker.setPosition(new LatLng(lat,longi));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(),14));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(lat,longi)));
        getLatLong();
    }

    void addMsg(){
        String msg = Edit_message.getText().toString();
        Message message = new Message(mAuth.getCurrentUser().getUid(),msg,utils.usernameFromEmail(mAuth.getCurrentUser().getEmail()));
        mDatabase.child(Fire_Tour).child(Fire_Discussion).child(lenght+"").setValue(message);
        Edit_message.setText("");
    }

    void updateLatLong(Intent i){
        longi = i.getDoubleExtra(GPSTracker.Broadcast_Longi,0);
        lat = i.getDoubleExtra(GPSTracker.Broadcast_Lat,0);

        setmarker();
    }

}
