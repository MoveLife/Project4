package com.resist.movelife;

import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

/**
 * Created by Thomas on 14-6-2014.
 */
public class ZoekBedrijven extends ListActivity implements
        AdapterView.OnItemClickListener {

    ContentValues cv = new ContentValues();
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.bedrijflist);
        LocalDatabaseConnector.init(this);

        Log.d("companysize", "" + Company.getCompanies().size());
        List<Company> lijst = Company.getCompanies();
        final CustomBaseAdapterAlleBedrijven adapter = new CustomBaseAdapterAlleBedrijven(this, lijst);
        listView = (ListView) findViewById(android.R.id.list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        listView.setTextFilterEnabled(true);
        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
        EditText editTxt = (EditText) findViewById(R.id.editTxt);
        editTxt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (count < before) {
                    // We're deleting char so we need to reset the adapter data
                    adapter.resetData();
                }


                adapter.getFilter().filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

        String name = String.valueOf(getListView().getItemAtPosition(position));
        Log.d("id", name);
        Intent intent = new Intent(this, ResultsInfoBedrijven.class);
        intent.putExtra("position", position);
        startActivity(intent);

    }
}
