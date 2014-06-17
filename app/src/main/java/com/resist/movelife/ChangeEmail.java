package com.resist.movelife;

import android.app.Activity;
import android.os.Bundle;

public class ChangeEmail extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changeemail);
        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
    }
}
