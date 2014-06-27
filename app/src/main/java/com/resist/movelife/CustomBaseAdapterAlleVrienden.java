package com.resist.movelife;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CustomBaseAdapterAlleVrienden extends BaseAdapter {
    private Context context;
    private List<User> users = new ArrayList<User>();

    public CustomBaseAdapterAlleVrienden(Context context, List<User> items) {
        this.context = context;
        this.users = items;

    }

    private class ViewHolder {
        TextView txtTitle;
        TextView tvLaatst;
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
              holder.txtTitle = (TextView) convertView.findViewById(R.id.tv_friends);
              holder.tvLaatst = (TextView) convertView.findViewById(R.id.tv_vriendlaatst);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        SimpleDateFormat df = new SimpleDateFormat("c d MMMM yyyy HH:mm");
        holder.txtTitle.setText(users.get(position).getEmail());

        holder.tvLaatst.setText(df.format(users.get(position).getLastSeen()));

        return convertView ;
    }
}
