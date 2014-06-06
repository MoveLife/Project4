package example.myapplication.movelife;

import android.app.Activity;
import android.os.Bundle;

public class Categories extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categories);
        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
    }
}
