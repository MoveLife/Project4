package com.resist.movelife;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas on 26-6-2014.
 */
public class CustomBaseAdapterAlleVriendenRequests extends BaseAdapter {
    private Context context;
    private List<User> users = new ArrayList<User>();

    public CustomBaseAdapterAlleVriendenRequests(Context context, List<User> items) {
        this.context = context;
        this.users = items;

    }

    private class ViewHolder {
        TextView txtEmail;
        Button buttonAccept;
        Button buttonWeiger;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.friendrequest_info, null);
            holder = new ViewHolder();
            assert convertView != null;
            holder.txtEmail = (TextView) convertView.findViewById(R.id.tv_friendsrequestnaam);
            holder.buttonAccept = (Button)convertView.findViewById(R.id.btn_friendsrequestaccept);
            holder.buttonWeiger = (Button)convertView.findViewById(R.id.btn_friendsrequestweiger);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.buttonAccept.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {

                       final Activity parent = (Activity)context;
                       new Thread(new Runnable() {
                           public void run() {
                               ServerConnection.acceptFriendRequest(users.get(position).getUid());

                               parent.runOnUiThread(new Runnable() {
                                   public void run() {

                                       Toast.makeText(parent.getBaseContext(), parent.getResources().getString(R.string.friend_added), Toast.LENGTH_LONG).show();
                                       LocalDatabaseConnector.restart();
                                       parent.finish();
                                   }

                               });

                           }
                       }).start();

                   }
               });


             holder.buttonWeiger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Activity parent = (Activity)context;
                new Thread(new Runnable() {
                    public void run() {
                        ServerConnection.removeFriend(users.get(position).getUid());

                        parent.runOnUiThread(new Runnable() {
                            public void run() {

                                Toast.makeText(parent.getBaseContext(), parent.getResources().getString(R.string.friend_declined), Toast.LENGTH_LONG).show();
                                parent.finish();
                            }

                        });

                    }
                }).start();
            }
        });
        holder.txtEmail.setText(users.get(position).getName());

        return convertView ;
    }
}