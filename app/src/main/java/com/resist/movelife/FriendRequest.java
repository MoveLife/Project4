package com.resist.movelife;

import android.app.ListActivity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.ListView;

import java.util.List;

public class FriendRequest extends ListActivity {
    private ListView listView;
    private CustomBaseAdapterAlleVriendenRequests adapter = null;
    private static List<User> users;
    private static boolean createNew = true;
    private static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friendrequest);

        adapter = new CustomBaseAdapterAlleVriendenRequests(this, users);
        listView = (ListView) findViewById(android.R.id.list);
        listView.setAdapter(adapter);
    }
    @Override
    protected void onStop() {
        super.onStop();
        createNew = true;
    }

    public static void setUsers(List<User> l) {
        users = l;
        if(createNew && context != null && !users.isEmpty()) {
            createNew = false;
            int reqs = users.size();
            String title = context.getResources().getString(R.string.notification_friend_request_title);
            String msg;
            if(reqs == 1) {
                msg = String.format(context.getResources().getString(R.string.notification_friend_request_msg_one), users.get(0).getName());
            } else {
                msg = String.format(context.getResources().getString(R.string.notification_friend_request_msg), reqs);
            }
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.icon)
                    .setContentTitle(title)
                    .setContentText(msg);
            TaskStackBuilder sb = TaskStackBuilder.create(context);
            Intent intent = new Intent(context,FriendRequest.class);
            sb.addParentStack(FriendRequest.class);
            sb.addNextIntent(intent);
            PendingIntent pi = sb.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pi);
            NotificationManager nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            nm.notify(0,builder.build());
        }
    }

    public static void init(Context c) {
        context = c;
    }

    public static boolean getCreateNew() {
        return createNew;
    }
}
