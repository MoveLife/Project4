package example.myapplication.movelife;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Friends extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bedrijflist);
        LayoutInflater li = getLayoutInflater();
        View v = li.inflate(R.layout.bedrijflist,null);

        ListView codeLearnLessons = (ListView)v.findViewById(R.id.lijst1);

        BedrijfCustomAdapter listAdapter = new BedrijfCustomAdapter(this);
        codeLearnLessons.setAdapter(listAdapter);


    }
}

class BedrijfCustomAdapter extends BaseAdapter {

    Company company = new Company(0, "Jan", 0, 0);
    List<Company> lijst = new ArrayList<Company>();

    private final Context context;

    public BedrijfCustomAdapter(Context context) {
        this.context = context;

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
    public View getView(int position, View view, ViewGroup parent) {


        if(view==null)
        {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.bedrijf_info, parent,false);
        }

        TextView bedrijfsnaam = (TextView)view.findViewById(R.id.Bedrijfsnaam);
        TextView bedrijfdesc = (TextView)view.findViewById(R.id.Bedrijfdesc);


        Company company = lijst.get(position); //lijst[position]
        Log.d("lolz",company.toString());
        bedrijfsnaam.setText(company.getName());
        if(company.getDescription() == null) {
            bedrijfdesc.setText(".");
        } else {
            bedrijfdesc.setText(company.getDescription());
        }
        return view;
    }


}


