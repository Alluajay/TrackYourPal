package com.example.allu.trackyourpal.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.allu.trackyourpal.POJO.User;
import com.example.allu.trackyourpal.R;
import com.example.allu.trackyourpal.UI.TourViewActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Set;

import static com.example.allu.trackyourpal.Utils.Attributes.Fire_Friends;
import static com.example.allu.trackyourpal.Utils.Attributes.Fire_Users;
import static com.example.allu.trackyourpal.Utils.Attributes.Intent_uid;
import static com.example.allu.trackyourpal.Utils.Attributes.Intent_username;


/**
 * Created by allu on 4/9/17.
 */

public class AdapterFriendRequests extends RecyclerView.Adapter<ViewHolderRequests> {
    ArrayList<User> userArrayList;
    Context context;
    FirebaseDatabase database;
    FirebaseAuth auth;

    public AdapterFriendRequests(Set<User> userArrayList, Context context, FirebaseDatabase database, FirebaseAuth auth) {
        this.userArrayList = new ArrayList<>(userArrayList);
        this.context = context;
        this.database = database;
        this.auth = auth;
    }
    public AdapterFriendRequests(ArrayList<User> userArrayList, Context context, FirebaseDatabase database, FirebaseAuth auth) {
        this.userArrayList
                = userArrayList;
        this.context = context;
        this.database = database;
        this.auth = auth;
    }

    @Override
    public ViewHolderRequests onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.single_friendrequest,parent,false);
        return new ViewHolderRequests(v);
    }

    @Override
    public void onBindViewHolder(ViewHolderRequests holder, int position) {
        final User user = userArrayList.get(position);
        holder.Text_Username.setText(user.Username);
        if(user.request == 0){
            holder.Btn_request.setVisibility(View.GONE);
            holder.Lay_Single.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, TourViewActivity.class);
                    i.putExtra(Intent_username,user.Username);
                    i.putExtra(Intent_uid,user.UID);
                    context.startActivity(i);
                }
            });
        }else if(user.request == 1){
            holder.Btn_request.setText(context.getString(R.string.AcceptFriend));
            holder.Btn_request.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    database.getReference().child(Fire_Users).child(user.UID).child(Fire_Friends).child(auth.getCurrentUser().getUid()).setValue(true);
                    database.getReference().child(Fire_Users).child(auth.getCurrentUser().getUid()).child(Fire_Friends).child(user.UID).setValue(true);
                }
            });
        }else if(user.request == 2){
            holder.Btn_request.setText(context.getString(R.string.RequestFriend));
            holder.Btn_request.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    database.getReference().child(Fire_Users).child(user.UID).child(Fire_Friends).child(auth.getCurrentUser().getUid()).setValue("req");
                    database.getReference().child(Fire_Users).child(auth.getCurrentUser().getUid()).child(Fire_Friends).child(user.UID).setValue("req");
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }
}

class ViewHolderRequests extends RecyclerView.ViewHolder{
    TextView Text_Username;
    Button Btn_request;
    LinearLayout Lay_Single;
    public ViewHolderRequests(View itemView) {
        super(itemView);
        Text_Username = (TextView)itemView.findViewById(R.id.txt_user);
        Btn_request = (Button)itemView.findViewById(R.id.btn_request);
        Lay_Single = (LinearLayout)itemView.findViewById(R.id.lay_singlefriendrequest);
    }
}
