package com.example.allu.trackyourpal.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.allu.trackyourpal.POJO.Message;
import com.example.allu.trackyourpal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.allu.trackyourpal.Utils.Attributes.Fire_Discussion;
import static com.example.allu.trackyourpal.Utils.Attributes.Fire_Tour;
import static com.example.allu.trackyourpal.Utils.Attributes.Fire_Users;

/**
 * Created by allu on 4/13/17.
 */

public class ListProvider implements RemoteViewsService.RemoteViewsFactory {
    ArrayList<Message> messageArrayList = new ArrayList<>();
    Context context = null;
    int appWidgetId;

    FirebaseDatabase database;
    FirebaseAuth auth;
    DatabaseReference reference;

    public ListProvider(Context context, Intent intent) {
        this.context = context;

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

        populateListItem();
    }

    void populateListItem(){
        reference.child(Fire_Users).child(auth.getCurrentUser().getUid()).child(Fire_Tour).child(Fire_Discussion).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Message message = snapshot.getValue(Message.class);
                    messageArrayList.add(message);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public RemoteViews getViewAt(int position) {
        final RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.single_message_widget);
        Message message = messageArrayList.get(position);
        remoteView.setTextViewText(R.id.txt_user, message.Username);
        remoteView.setTextViewText(R.id.txt_message, message.Message);
        return remoteView;
    }


    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return messageArrayList.size();
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
