package com.resist.movelife;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ChangeEmail extends Activity{
    private String currentEmail = Menu.currentEmail();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changeemail);
        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);

        if(Menu.isConnected()) {
            TextView CurrentEmail = (TextView) findViewById(R.id.tv_CurrentEmail);

            CurrentEmail.setText(currentEmail);
        } else {
            TextView t = (TextView) findViewById(R.id.tv_CurrentEmail);
            Button b = (Button) findViewById(R.id.btn_ChangeEmail);
            b.setVisibility(View.GONE);
            t.setText("Geen internetverbinding");
        }
    }

    private void changeEmail(View v) {
        EditText email = (EditText)findViewById(R.id.et_Email);
        EditText emailVerify = (EditText)findViewById(R.id.et_EmailVerify);

        String e = email.getText().toString();
        String eVerify = emailVerify.getText().toString();

        if (e.equals(eVerify)){
            Toast.makeText(getApplicationContext(), "Uw email is veranderd", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Uw email is niet gelijk", Toast.LENGTH_SHORT).show();
        }
    }
}
