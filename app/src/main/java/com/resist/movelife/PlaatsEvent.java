package com.resist.movelife;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Thomas on 25-6-2014.
 */
public class PlaatsEvent extends Activity {

    Button button;
    EditText etNaam, etDesc;
    DatePicker datePicker, datePicker1;
    TimePicker timePicker, timePicker1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plaats_event);
        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);

        etNaam = (EditText) findViewById(R.id.et_eventNaam);
        etDesc = (EditText) findViewById(R.id.et_eventDesc);
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        datePicker1 = (DatePicker) findViewById(R.id.datePicker2);
        timePicker1 = (TimePicker) findViewById(R.id.timePicker2);
        timePicker.setIs24HourView(true);
        timePicker1.setIs24HourView(true);
        button = (Button) findViewById(R.id.btn_maakEvent);
        final PlaatsEvent parent = this;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


         final String en = etNaam.getText().toString();
         final String ed = etDesc.getText().toString();

                Calendar c = Calendar.getInstance();
                c.set(datePicker.getYear(),datePicker.getMonth(),datePicker.getDayOfMonth(),timePicker.getCurrentHour(),timePicker.getCurrentMinute());
                final Date startd = (c.getTime());

                Calendar calendar = Calendar.getInstance();
                calendar.set(datePicker1.getYear(),datePicker1.getMonth(),datePicker1.getDayOfMonth(),timePicker1.getCurrentHour(),timePicker1.getCurrentMinute());
                final Date eindd = (calendar.getTime());





                new Thread(new Runnable() {
                    public void run() {
                     final boolean b = Menu.getUpdater().addEvent(en,ResultsInfoBedrijven.filteredCompany.getBid(),startd, eindd, ed);
                        parent.runOnUiThread(new Runnable() {
                            public void run() {
                                if (b) {
                                    Toast.makeText(parent.getBaseContext(), "Evenement gemaakt!", Toast.LENGTH_LONG).show();
                                    Event.createEventList();

                                } else {
                                    Toast.makeText(parent.getBaseContext(), "Evenement is niet aangemaakt", Toast.LENGTH_LONG).show();
                                }
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
