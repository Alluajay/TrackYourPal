package com.example.allu.trackyourpal.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.allu.trackyourpal.POJO.Message;
import com.example.allu.trackyourpal.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

/**
 * Created by allu on 4/9/17.
 */

public class Adapter_Message extends RecyclerView.Adapter<ViewHolder_Message>{
    Context context;
    ArrayList<Message> messages;
    FirebaseAuth mAuth;

    public Adapter_Message(Context context, ArrayList<Message> messages, FirebaseAuth mAuth) {
        this.context = context;
        this.messages = messages;
        this.mAuth = mAuth;
    }

    @Override
    public ViewHolder_Message onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.single_message,parent,false);
        return new ViewHolder_Message(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder_Message holder, int position) {
        Message message = messages.get(position);
        if(message.Uid.equals( mAuth.getCurrentUser().getUid())){
            holder.User.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            holder.Message.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        }
        holder.User.setText(message.Username);
        holder.Message.setText(message.Message);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void setMessage(Message message) {
        messages.add(message);
        this.notifyDataSetChanged();
    }
}

class ViewHolder_Message extends RecyclerView.ViewHolder{
    TextView User,Message;
    public ViewHolder_Message(View itemView) {
        super(itemView);
        User = (TextView)itemView.findViewById(R.id.txt_user);
        Message = (TextView)itemView.findViewById(R.id.txt_message);
    }
}
