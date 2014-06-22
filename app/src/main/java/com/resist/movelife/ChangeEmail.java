package com.resist.movelife;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ChangeEmail extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changeemail);
        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
    }

    private void changeEmail(View v) {
        EditText Email = (EditText)findViewById(R.id.et_Email);
        EditText EmailVerify = (EditText)findViewById(R.id.et_EmailVerify);

        if (Email.getText().toString().equals(EmailVerify.getText().toString())) {
            //Doorgaan, oftewel t is goed.
            Toast.makeText(getApplicationContext(), "Uw email is veranderd", Toast.LENGTH_SHORT).show();
            //finish();
        }
    }
}
