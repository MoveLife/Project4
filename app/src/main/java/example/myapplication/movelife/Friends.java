package example.myapplication.movelife;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public  class Friends extends Activity {

    static LocalDatabaseConnector localDatabaseConnector = new LocalDatabaseConnector();





    ListView listView;
    List<Company> lijst = new ArrayList<Company>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bedrijflist);

        ContentValues contentValues = new ContentValues();
        contentValues.put("bid", 1);
        contentValues.put("name", "Jan");
        contentValues.put("latiude", 1.2);
        contentValues.put("longitude", 1.3);
        contentValues.put("cid", 1);
        contentValues.put("postcode", 3124);
        contentValues.put("address", "wegisweg");
        contentValues.put("rating", 2);
        contentValues.put("tid", 2);
        contentValues.put("telephone", 21412);
        contentValues.put("description", "Hele leuke tent");
        contentValues.put("buystate", 1);


        localDatabaseConnector.insert("companies", null, contentValues);


        listView = (ListView) findViewById(R.id.lijst1);
        CustomBaseAdapter adapter = new CustomBaseAdapter(this, lijst);
        listView.setAdapter(adapter);




    }

}






