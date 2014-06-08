package example.myapplication.movelife;


import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.widget.ListView;

import java.util.List;

public class Events extends Activity{

    ContentValues cv = new ContentValues();
    ListView listView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.events);
        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);

        setContentView(R.layout.alle_bedrijf_info_list);
        LocalDatabaseConnector.init(this);
        List<Company> lijst = Company.getCompanies();


        listView = (ListView) findViewById(R.id.listView);



        CustomBaseAdapterAlleBedrijfInfo adapter = new CustomBaseAdapterAlleBedrijfInfo(this, lijst);
        listView.setAdapter(adapter);





















    }
}
