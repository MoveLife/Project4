package com.resist.movelife;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;



public class CustomBaseAdapterAlleBedrijven extends BaseAdapter implements Filterable {
    Context context;
    List<Company> lijst = new ArrayList<Company>();
    private List<Company> lijstOrig;
    private Filter bedrijfFilter;


    public CustomBaseAdapterAlleBedrijven(Context context, List<Company> items) {
        this.context = context;
        this.lijst = items;
        this.lijstOrig = items;
    }
    public void resetData() {
        lijst = lijstOrig;
    }

    /*private view holder class*/
    private class ViewHolder {

        TextView txtTitle;
        TextView txtDesc;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.bedrijf_info, null);
            holder = new ViewHolder();
            assert convertView != null;
            holder.txtDesc = (TextView) convertView.findViewById(R.id.Bedrijfdesc);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.Bedrijfsnaam);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        Company company = (Company) getItem(position);
        holder.txtDesc.setText(company.getDescription());
        holder.txtTitle.setText(company.getName());

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

    @Override
    public Filter getFilter() {

        if (bedrijfFilter == null)
            bedrijfFilter = new BedrijfFilter();
        return bedrijfFilter;
    }

    private class BedrijfFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();


            // We implement here the filter logic
            if (constraint == null || constraint.length() == 0) {
                // No filter implemented we return all the list
                results.values = lijst;
                results.count = lijst.size();
            }
            else {
                // We perform filtering operation
                List<Company> nLijst = new ArrayList<Company>();

                for (Company p : lijst) {
                    if (p.getName().toUpperCase().startsWith(constraint.toString().toUpperCase()))
                        nLijst.add(p);
                }
                results.values =  nLijst;
                results.count = nLijst.size();

            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            // Now we have to inform the adapter about the new list filtered
            if (results.count == 0)
                notifyDataSetInvalidated();
            else {
                lijst = (List<Company>) results.values;
                notifyDataSetChanged();
            }


        }
    }

}
