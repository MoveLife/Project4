package com.resist.movelife;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;



public class Events extends Activity {

    //ContentValues cv = new ContentValues();
    // ListView listView;
    private Company company;
    //Map map = new Map();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  setContentView(R.layout.events);
        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
        setContentView(R.layout.alle_bedrijf_info);
        int pos = getIntent().getIntExtra("position",-1);
        company = Company.getCompanies().get(pos);
        TextView tv = (TextView) findViewById(R.id.tv_bedrijfsnaam);
        tv.setText(company.getName());

        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //map.setGoToLocation(latitude, longitude);

               // Log.d("Checkingla", ""+latitude);
              //  Log.d("Checkinglo", ""+longitude);



                Intent intent = new Intent(getBaseContext(),Map.class);
                intent.putExtra("latitude", company.getLatitude());
                intent.putExtra("longitude", company.getLongitude());
               // intent.setClass(getBaseContext(), Map.class);
               startActivity(intent);

            }
        });

    }
}
