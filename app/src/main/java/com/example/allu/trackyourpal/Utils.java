package com.example.allu.trackyourpal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;


/**
 * Created by allu on 2/14/17.
 */

public class Utils {
    public static String pref_string = "Attendance";

    Context mContext;
    SharedPreferences preferences;

    public Utils(Context context){
        mContext = context;
        preferences = mContext.getSharedPreferences(pref_string,Context.MODE_PRIVATE);
    }

    public void Goto(Class cls){
        Intent i=new Intent(mContext,cls);
        mContext.startActivity(i);
    }

    public boolean isEmptyString(String... Args){
        for (String arg : Args){
            if(arg.trim().equals("") || arg == null){
                return false;
            }
        }
        return true;
    }

    public boolean isEmptyint(int... Args){
        for (int arg : Args){
            if(arg == 0){
                return false;
            }
        }
        return true;
    }

    public boolean isEmptylong(long... Args){
        for (long arg : Args){
            if(arg == 0){
                return false;
            }
        }
        return true;
    }

    public String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    public void Toast(String msg){
        Toast.makeText(mContext,msg,Toast.LENGTH_SHORT).show();
    }
}
