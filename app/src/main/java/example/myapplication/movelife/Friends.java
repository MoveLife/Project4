package example.myapplication.movelife;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public  class Friends extends Activity {


    ListView listView;
    List<Company> lijst = new ArrayList<Company>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bedrijflist);

        listView = (ListView) findViewById(R.id.lijst1);
        CustomBaseAdapter adapter = new CustomBaseAdapter(this, lijst);
        listView.setAdapter(adapter);

    }

}






