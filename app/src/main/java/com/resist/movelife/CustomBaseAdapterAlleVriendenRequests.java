package com.resist.movelife;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas on 26-6-2014.
 */
public class CustomBaseAdapterAlleVriendenRequests extends BaseAdapter {

    Context context;
    List<User> users = new ArrayList<User>();


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
    public View getView(int position, View convertView, ViewGroup viewGroup) {


        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.friends_info, null);
            holder = new ViewHolder();
            assert convertView != null;
            holder.txtEmail = (TextView) convertView.findViewById(R.id.tv_friendsrequestnaam);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.txtEmail.setText(users.get(position).getEmail());



        return convertView ;
    }





}
