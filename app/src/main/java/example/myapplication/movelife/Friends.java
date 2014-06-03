package example.myapplication.movelife;


import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Friends extends Activity {

    Company company = new Company(0, null, 0, 0, null, null, null, null, null, null, null, null);


    ListView bedrijfnaamlijst, bedrijfsfoto;
    ArrayAdapter arrayAdapter, arrayAdapter1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bedrijf_info);
        String mijnlijst = company.getName();

        bedrijfnaamlijst = (ListView) findViewById(R.id.bedrijfnaam);


        TextView emptyText = (TextView) findViewById(android.R.id.empty);


        if (mijnlijst == null) {

            bedrijfnaamlijst.setEmptyView(emptyText);
            bedrijfsfoto.setEmptyView(emptyText);

        } else {

            arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, mijnlijst.length());



            bedrijfnaamlijst.setAdapter(arrayAdapter);



        }

    }
}
