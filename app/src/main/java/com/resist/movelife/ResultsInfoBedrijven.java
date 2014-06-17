package com.resist.movelife;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Thomas on 14-6-2014.
 */
public class ResultsInfoBedrijven extends Activity {


    private Company company;
    public static Company filteredCompany = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
        setContentView(R.layout.alle_bedrijf_info);
       // int pos = getIntent().getIntExtra("position",-1);
        company = filteredCompany;
        TextView tv = (TextView) findViewById(R.id.tv_bedrijfsnaam);
        TextView tvdesc = (TextView) findViewById(R.id.tv_bedrijfsinfo);
        tv.setText(company.getName());
        tvdesc.setText(company.getDescription());

        Button reviewButton = (Button) findViewById(R.id.reviewBtn);
        reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(),PlaatsReview.class);
                startActivity(intent);
            }
        });


        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(getBaseContext(),Map.class);
                intent.putExtra("latitude", company.getLatitude());
                intent.putExtra("longitude", company.getLongitude());
                startActivity(intent);

            }
        });
    }
}
