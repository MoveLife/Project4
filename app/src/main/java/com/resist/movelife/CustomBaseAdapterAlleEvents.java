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

/**
 * Created by Thomas on 24-6-2014.
 */
public class CustomBaseAdapterAlleEvents extends BaseAdapter {

    Context context;
    List<Event> events = new ArrayList<Event>();


    public CustomBaseAdapterAlleEvents(Context context, List<Event> items) {
        this.context = context;
        this.events = items;
    }

    private class ViewHolder {
        TextView eventTitle;
        TextView eventDesc;
        TextView eventBedrijf;
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public Object getItem(int position) {
        return events.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.events_info, null);
            holder = new ViewHolder();
            assert convertView != null;
            holder.eventTitle = (TextView) convertView.findViewById(R.id.tv_eventnaam);
            holder.eventDesc = (TextView) convertView.findViewById(R.id.tv_eventdescription);
            holder.eventBedrijf = (TextView) convertView.findViewById(R.id.tv_eventvanbedrijf);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

       //  holder.eventTitle.setText();

        return convertView ;

    }
}
