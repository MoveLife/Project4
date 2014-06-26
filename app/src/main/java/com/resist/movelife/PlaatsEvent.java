package com.resist.movelife;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Thomas on 25-6-2014.
 */
public class PlaatsEvent extends Activity {

    Button button;
    EditText etNaam, etDesc;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plaats_event);
        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);

        etNaam = (EditText) findViewById(R.id.et_eventNaam);
        etDesc = (EditText) findViewById(R.id.et_eventDesc);

        button = (Button) findViewById(R.id.btn_maakEvent);
        final PlaatsEvent parent = this;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


         final String en = etNaam.getText().toString();
         final String ed = etDesc.getText().toString();

                Calendar c = Calendar.getInstance();
                c.set(datePicker.getYear(),datePicker.getMonth(),datePicker.getDayOfMonth(),timePicker.getCurrentHour(),timePicker.getCurrentMinute());
                Date d = new Date(d.getTime());


                new Thread(new Runnable() {
                    public void run() {
                       ServerConnection.addEvent(en,ResultsInfoBedrijven.filteredCompany.getBid(),datePicker, etEind, ed);
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
