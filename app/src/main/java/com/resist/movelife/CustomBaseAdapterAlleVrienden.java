package com.resist.movelife;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CustomBaseAdapterAlleVrienden extends BaseAdapter {


    Context context;
   List<Friends> lijst = new ArrayList<Friends>();

    public CustomBaseAdapterAlleVrienden(Context context, List<Company> items) {
        this.context = context;
        //this.lijst = items;

    }

    private class ViewHolder {
        TextView txtTitle;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {


        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.friends_info, null);
            holder = new ViewHolder();
            assert convertView != null;
         //   holder.txtTitle = (TextView) convertView.findViewById(R.id.tv_friends);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        // holder.txtTitle.setText(user.getName());

        return convertView ;
    }
}
