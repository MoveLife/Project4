package example.myapplication.movelife;


import android.app.Activity;

import android.app.ListActivity;
import android.content.ContentValues;

import android.content.Intent;

import android.os.Bundle;

import android.util.Log;



import android.view.View;

import android.widget.AdapterView;

import android.widget.EditText;

import android.widget.ListView;
import android.widget.SearchView;

import java.util.List;

public  class Friends extends ListActivity implements

        AdapterView.OnItemClickListener{
    public final static String ID_EXTRA = "";
    public final static String ID_EXTRA1 = "";
    // Search EditText
    EditText inputSearch;
    private SearchView searchView;




    ContentValues cv = new ContentValues();
    ListView listView;
   // List<Company> lijst = new ArrayList<Company>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bedrijflist);
        LocalDatabaseConnector.init(this);


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


        Log.d("companysize", "" + Company.getCompanies().size());
        List<Company> lijst = Company.getCompanies();
        CustomBaseAdapterAlleBedrijven adapter = new CustomBaseAdapterAlleBedrijven(this, lijst);


        listView = (ListView) findViewById(android.R.id.list);


    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

      String name = String.valueOf(getListView().getItemAtPosition(position));
        Log.d("id", name);
       // Object color = parent.getAdapter().getItem(position);


        // listView.setChoiceMode(listView.CHOICE_MODE_SINGLE);
        //  listView.setSelection(position);
        //    HashMap<String, String> elt = (HashMap<String, String>) listView.getItemAtPosition(position);


          Intent intent = new Intent(this, Events.class);
          intent.putExtra(ID_EXTRA, Company.getCompanies().get(0).getName() );
          intent.putExtra(ID_EXTRA1, Company.getCompanies().get(0).getDescription() );
          startActivity(intent);

    }







}






