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

        String E = Email.getText().toString();
        String EVerify = EmailVerify.getText().toString();

        if (E.equals(EVerify)){
            Toast.makeText(getApplicationContext(), "Uw email is veranderd", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Uw email is niet gelijk", Toast.LENGTH_SHORT).show();
        }
    }
}
