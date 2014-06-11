package com.resist.movelife;


import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;



public class Events extends Activity {

    //ContentValues cv = new ContentValues();
    // ListView listView;

    String passedVar = null;
    private TextView passedView = null;

    String passedVar1 = null;
    private TextView passedView1 = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  setContentView(R.layout.events);
       // overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
        setContentView(R.layout.alle_bedrijf_info);

        passedVar = getIntent().getStringExtra(Friends.ID_EXTRA);
       // passedVar1 = getIntent().getStringExtra(Friends.ID_EXTRA1);


        passedView = (TextView) findViewById(R.id.tv_bedrijfsnaam);
        passedView.setText(passedVar);

       // passedView1 = (TextView) findViewById(R.id.tv_bedrijfsinfo);
       // passedView1.setText(passedVar1);


        // LocalDatabaseConnector.init(this);
        // List<Company> lijst = Company.getCompanies();


        //  listView = (ListView) findViewById(R.id.listView);


        //  CustomBaseAdapterAlleBedrijfInfo adapter = new CustomBaseAdapterAlleBedrijfInfo(this, lijst);
//        listView.setAdapter(adapter);


    }
}
