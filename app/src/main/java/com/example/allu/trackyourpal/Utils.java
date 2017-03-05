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

    public void Logout(){
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("uid");
        editor.commit();
       // Goto(LoginActivity.class);
    }

    public void comm_logout(){
        if(preferences.contains("uid")){
            Logout();
        }else if(preferences.contains("id")){
            Logout_stud();
        }
    }

    public void Logout_stud(){
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("id");
        editor.remove("rno");
        editor.remove("name");
        editor.remove("year");
        editor.remove("dept");
        editor.commit();
       // Goto(LoginActivity.class);
    }

    public void Toast(String msg){
        Toast.makeText(mContext,msg,Toast.LENGTH_SHORT).show();
    }
}
