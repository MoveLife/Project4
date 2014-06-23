package com.resist.movelife;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.Menu;
import android.view.MenuItem;
import com.resist.movelife.R;

import java.util.List;

public class FriendRequest extends Activity {
    private static List<User> users;
    private static boolean createNew = true;
    private static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friendrequest);
        createNew = true;
    }

    public static void setUsers(List<User> l) {
        users = l;
        if(createNew && context != null && !l.isEmpty()) {
            createNew = false;
            //notifications
            String msg = String.format(context.getResources().getString(R.string.notification_friend_request_msg),users.size());
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.icon)
                    .setContentTitle("@string/notification_friend_request_title")
                    .setContentText(msg);
            TaskStackBuilder sb = TaskStackBuilder.create(context);
            sb.addParentStack(FriendRequest.class);
            Intent intent = new Intent(context,FriendRequest.class);
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
}
