package com.resist.movelife;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;



/**
 * Created by Thomas on 8-6-2014.
 */
public class CustomBaseAdapterAlleBedrijfInfo extends BaseAdapter {

    Context context;
    List<Company> lijst = new ArrayList<Company>();

    public CustomBaseAdapterAlleBedrijfInfo(Context context, List<Company> items) {
        this.context = context;
        this.lijst = items;
    }

    /*private view holder class*/
    private class ViewHolder {
        ImageView imageView;
        TextView txttitle;
        TextView txtdesc;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.alle_bedrijf_info, null);
            holder = new ViewHolder();
            assert convertView != null;
            holder.txtdesc = (TextView) convertView.findViewById(R.id.tv_bedrijfsinfo);
            holder.txttitle = (TextView) convertView.findViewById(R.id.tv_bedrijfsnaam);
            //holder.imageView = (ImageView) convertView.findViewById(R.id.imageView1);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        Company company = (Company) getItem(position);

        holder.txtdesc.setText(company.getDescription());
        holder.txttitle.setText(company.getName());
        //holder.imageView.setImageResource(R.id.imageView1);

        return convertView;
    }

    @Override
    public int getCount() {
        return lijst.size();
    }

    @Override
    public Object getItem(int position) {
        return lijst.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}


