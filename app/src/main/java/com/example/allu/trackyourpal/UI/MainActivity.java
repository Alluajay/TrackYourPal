package com.example.allu.trackyourpal.UI;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.allu.trackyourpal.GPS.GPS_Util;
import com.example.allu.trackyourpal.R;
import com.example.allu.trackyourpal.Utils;

public class MainActivity extends AppCompatActivity {
    final int PERMISSION_ACCESS_COARSE_LOCATION = 1;

    Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        utils = new Utils(this);
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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_COARSE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    GPS_Util.setGPS_Status_Flag(true);
                } else {
                    GPS_Util.setGPS_Status_Flag(false);
                }
                return;
            }
        }
    }
    @Override
    public void onBackPressed() {
        finish();
        return;
    }
}
