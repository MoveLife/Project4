package com.resist.movelife;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Thomas on 25-6-2014.
 */
public class PlaatsEvent extends Activity {

    Button button;
    EditText etNaam, etDesc, etStart, etEind;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plaats_event);
        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);


        etNaam = (EditText) findViewById(R.id.et_eventNaam);
        etDesc = (EditText) findViewById(R.id.et_eventDesc);
        etStart = (EditText) findViewById(R.id.et_eventStart);
        etEind = (EditText) findViewById(R.id.et_eventEind);
        button = (Button) findViewById(R.id.btn_maakEvent);
        final PlaatsEvent parent = this;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                new Thread(new Runnable() {
                    public void run() {
                       // DatabaseUpdater.addReview(ResultsInfoBedrijven.filteredCompany.getBid(), rating, reviews);
                        parent.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(parent.getBaseContext(), "Evenement gemaakt!", Toast.LENGTH_LONG).show();
                                parent.finish();
                                button.setVisibility(View.GONE);
                            }
                        });
                    }
                }).start();

            }
        });


    }


}
