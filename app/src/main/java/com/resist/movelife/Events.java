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

    String passedVar = null;
    private TextView passedView = null;

    String passedVar1 = null;
    private TextView passedView1 = null;
    Button button;
    //Map map = new Map();

    double latitude = Company.getCompanies().get(0).getLatitude();
    double longitude = Company.getCompanies().get(0).getLongitude();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  setContentView(R.layout.events);
        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
        setContentView(R.layout.alle_bedrijf_info);
        passedVar = getIntent().getStringExtra(Friends.ID_EXTRA);
        passedView = (TextView) findViewById(R.id.tv_bedrijfsnaam);
        passedView.setText(passedVar);

      button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //map.setGoToLocation(latitude, longitude);

               // Log.d("Checkingla", ""+latitude);
              //  Log.d("Checkinglo", ""+longitude);



                Intent intent = new Intent(getBaseContext(),Map.class);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
               // intent.setClass(getBaseContext(), Map.class);
               startActivity(intent);

            }
        });

    }
}
