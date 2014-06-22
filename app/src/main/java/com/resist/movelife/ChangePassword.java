package com.resist.movelife;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangePassword extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changepassword);
        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
    }

    public void checkPassword(View v) {
        EditText OldPassword = (EditText)findViewById(R.id.et_OldPassword);
        EditText NewPassword = (EditText)findViewById(R.id.et_NewPassword);
        EditText PasswordVerify = (EditText)findViewById(R.id.et_PasswordVerify);
        Button ChangePassword = (Button)findViewById(R.id.btn_ChangePassword);

        if (NewPassword.getText().toString().equals(PasswordVerify.getText().toString())) {
            //Doorgaan, oftewel t is goed.
            Toast.makeText(getApplicationContext(), "Uw wachtwoord is veranderd", Toast.LENGTH_SHORT).show();
            //finish();
        } else if (OldPassword.getText().toString().equals(NewPassword.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Uw wachtwoord is hetzelfde als de oude", Toast.LENGTH_SHORT).show();
        } else if (!NewPassword.getText().toString().equals(PasswordVerify.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Uw nieuwe wachtwoorden zijn niet gelijk", Toast.LENGTH_SHORT).show();
        }
    }
}
