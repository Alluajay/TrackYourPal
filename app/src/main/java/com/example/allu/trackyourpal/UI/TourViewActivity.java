package com.example.allu.trackyourpal.UI;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.allu.trackyourpal.Adapter.Adapter_Message;
import com.example.allu.trackyourpal.GPS.GPSTracker;
import com.example.allu.trackyourpal.POJO.Message;
import com.example.allu.trackyourpal.R;
import com.example.allu.trackyourpal.Utils;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
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

import static com.example.allu.trackyourpal.User_Utils.Attributes.Fire_Discussion;
import static com.example.allu.trackyourpal.User_Utils.Attributes.Fire_Lat;
import static com.example.allu.trackyourpal.User_Utils.Attributes.Fire_Long;
import static com.example.allu.trackyourpal.User_Utils.Attributes.Fire_Online;
import static com.example.allu.trackyourpal.User_Utils.Attributes.Fire_Tour;
import static com.example.allu.trackyourpal.User_Utils.Attributes.Fire_Users;
import static com.example.allu.trackyourpal.User_Utils.Attributes.Intent_uid;


public class TourViewActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

    String TAG = TourViewActivity.class.getSimpleName();
    Utils utils;
    MapFragment mapFragment;

    String Uid;

    DatabaseReference mDatabase;
    FirebaseAuth mAuth;

    double lat,longi;
    LatLng latLng;

    GoogleMap googleMap;
    Marker marker;

    EditText Edit_message;

    RecyclerView Recy_messages;
    ArrayList<Message> messages;
    Adapter_Message adapter_message;
    long lenght;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_view);
        utils = new Utils(this);
        //Uid = getIntent().getStringExtra(Intent_uid);

        mAuth = FirebaseAuth.getInstance();


        Edit_message = (EditText)findViewById(R.id.edit_msg);

        Recy_messages = (RecyclerView)findViewById(R.id.recy_message);
        Recy_messages.setHasFixedSize(false);
        Recy_messages.setItemAnimator(new DefaultItemAnimator());
        Recy_messages.setLayoutManager(new GridLayoutManager(this,1));

        messages = new ArrayList<>();
        adapter_message = new Adapter_Message(this,messages,mAuth);
        Recy_messages.setAdapter(adapter_message);


        Uid = getIntent().getStringExtra(Intent_uid);
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Fire_Users).child(Uid);
        mDatabase.child(Fire_Online).setValue(true);


        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
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
                adapter_message = new Adapter_Message(getApplicationContext(),messages,mAuth);
                Recy_messages.setAdapter(adapter_message);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mDatabase.child(Fire_Online).setValue(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDatabase.child(Fire_Online).setValue(false);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mDatabase.child(Fire_Online).setValue(true);
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
        marker.setPosition(new LatLng(lat,longi));
        googleMap.setBuildingsEnabled(true);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(),14));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(lat,longi)));
        getLatLong();
    }

    void AddMsg(){
        String msg = Edit_message.getText().toString();
        Message message = new Message(mAuth.getCurrentUser().getUid(),msg,utils.usernameFromEmail(mAuth.getCurrentUser().getEmail()));
        mDatabase.child(Fire_Tour).child(Fire_Discussion).child(lenght+"").setValue(message);
        Edit_message.setText("");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab_sendmsg:
                AddMsg();
                break;
        }
    }
}
