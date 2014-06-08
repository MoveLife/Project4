package example.myapplication.movelife;


import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public  class Friends extends ListActivity implements
        AdapterView.OnItemClickListener {

    ContentValues cv = new ContentValues();
    ListView listView;
   // List<Company> lijst = new ArrayList<Company>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.bedrijflist);
        LocalDatabaseConnector.init(this);
        List<Company> lijst = Company.getCompanies();
       //cv.put("bid", 10);
       // cv.put("name", "Mijnbedrijf" );
       // cv.put("latitude", 1.1);
       // cv.put("longitude", 1.2);
       // cv.put("cid", 1);
       // cv.put("postcode", 3124);
       // cv.put("address", 12);
       // cv.put("rating", 1.1);
       // cv.put("telephone", 321);
       // cv.put("description", "Goed");
       // cv.put("buystate", 1);


      //  LocalDatabaseConnector.insert("companies", null, cv);
       // Log.d("lijst", lijst.toString());
       // Log.d("lol",""+ LocalDatabaseConnector.insert("companies", null, cv));
        Log.d("companysize", ""+ Company.getCompanies().size());




            listView = (ListView) findViewById(android.R.id.list);



        CustomBaseAdapterAlleBedrijven adapter = new CustomBaseAdapterAlleBedrijven(this, lijst);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        //setContentView(R.layout.friends);
        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

        Intent intent = new Intent(this, Events.class);
        startActivity(intent);

    }


}






