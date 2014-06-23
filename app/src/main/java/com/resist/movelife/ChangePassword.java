package com.resist.movelife;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
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

        String OldPw = OldPassword.getText().toString();
        String NewPw = NewPassword.getText().toString();
        String PwVerify = PasswordVerify.getText().toString();

        if (OldPw.equals(NewPw)) {
            Toast.makeText(getApplicationContext(), "Uw wachtwoord is hetzelfde als de oude", Toast.LENGTH_SHORT).show();
        } else if (NewPw.equals(PwVerify)) {
            //Doorgaan, oftewel t is goed.
            Toast.makeText(getApplicationContext(), "Uw wachtwoord is veranderd", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Uw nieuwe wachtwoorden zijn niet gelijk", Toast.LENGTH_SHORT).show();
        }
    }
}