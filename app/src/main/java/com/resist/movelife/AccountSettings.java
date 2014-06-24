package com.resist.movelife;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class AccountSettings extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accountsettings);
        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);

        TextView CurrentEmail = (TextView)findViewById(R.id.tv_CurrentEmail);
        CurrentEmail.setText(Menu.currentEmail());
    }

    public void act_changeEmail (View view) {
        Intent intent = new Intent(this, ChangeEmail.class);
        startActivity(intent);
    }

    public void act_changePassword (View view) {
        Intent intent = new Intent(this, ChangePassword.class);
        startActivity(intent);
    }
}
