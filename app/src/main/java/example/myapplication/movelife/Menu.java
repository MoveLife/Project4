package example.myapplication.movelife;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Menu extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
    }

    public void act_Login (View view) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
    public void act_Map (View view) {
        Intent intent = new Intent(this, Map.class);
        startActivity(intent);
    }
    public void act_Status (View view) {
        Intent intent = new Intent(this, Status.class);
        startActivity(intent);
    }
    public void act_Categories (View view) {
        Intent intent = new Intent(this, Categories.class);
        startActivity(intent);
    }
    public void act_Friends (View view) {
        Intent intent = new Intent(this, Friends.class);
        startActivity(intent);
    }
    public void act_Events (View view) {
        Intent intent = new Intent(this, Events.class);
        startActivity(intent);
    }
}