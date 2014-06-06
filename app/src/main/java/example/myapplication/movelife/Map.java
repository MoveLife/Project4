package example.myapplication.movelife;


import android.app.Activity;
import android.os.Bundle;

public class Map extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
    }
}
