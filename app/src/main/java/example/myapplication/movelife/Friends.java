package example.myapplication.movelife;


import android.app.Activity;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
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


        Log.d("companysize", ""+ Company.getCompanies().size());
        List<Company> lijst = Company.getCompanies();
        CustomBaseAdapterAlleBedrijven adapter = new CustomBaseAdapterAlleBedrijven(this, lijst);


        listView = (ListView) findViewById(android.R.id.list);


        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        //setContentView(R.layout.friends);
        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);

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






