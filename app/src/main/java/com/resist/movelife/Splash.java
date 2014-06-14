package com.resist.movelife;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(Splash.this,Menu.class);
                startActivity(i);
                finish();
            }
        },3000);
    }
}

