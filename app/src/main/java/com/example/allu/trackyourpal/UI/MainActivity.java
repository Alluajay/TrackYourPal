package com.example.allu.trackyourpal.UI;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.allu.trackyourpal.R;
import com.example.allu.trackyourpal.Utils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    final int PERMISSION_ACCESS_COARSE_LOCATION = 1;

    Utils utils;

    Button GOTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        utils = new Utils(this);

        GOTO = (Button)findViewById(R.id.btn_GeoActivity);
        GOTO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        RequestPermission();
    }

    void RequestPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                utils.Toast("This application requires access to Location Service.");
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, PERMISSION_ACCESS_COARSE_LOCATION);
            }
        }
    }

    void GpsFunction(){
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!enabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }else{

        }
    }




    @Override
    public void onBackPressed() {
        finish();
        return;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_addFriend:
                utils.Goto(AddFriend.class);
                break;

            case R.id.btn_viewFriends:
                utils.Goto(HomeActivity.class);
                break;

            case R.id.btn_GeoActivity:

                break;


            case R.id.btn_friendstour:
                utils.Goto(FriendsTourActivity.class);
                break;
            case R.id.btn_yourtour:
                utils.Goto(TourViewActivity.class);
                break;
        }
    }
}
