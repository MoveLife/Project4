package com.resist.movelife;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ChangePassword extends Activity{
    private String getUserSetPassword = Menu.getUpdater().getUserSetPassword();
    private String getPassword = Menu.getUpdater().getPassword();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changepassword);
        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);

        if(!Menu.getUpdater().isConnected()) {
            Button btn_ChangePassword = (Button) findViewById(R.id.btn_ChangePassword);
            TextView noInternet = (TextView)findViewById(R.id.tv_noInternet);
            noInternet.setVisibility(View.VISIBLE);
            noInternet.setText("U heeft geen internet");
            btn_ChangePassword.setVisibility(View.GONE);
        }
    }

    public void checkPassword(View v) {
        EditText OldPassword = (EditText)findViewById(R.id.et_OldPassword);
        EditText NewPassword = (EditText)findViewById(R.id.et_NewPassword);
        EditText PasswordVerify = (EditText)findViewById(R.id.et_PasswordVerify);

        String OldPw = OldPassword.getText().toString();
        String NewPw = NewPassword.getText().toString();
        String PwVerify = PasswordVerify.getText().toString();

        if (OldPw != null && NewPw != null && PwVerify != null &&  OldPw.equals(NewPw)) {
            Toast.makeText(getApplicationContext(), "Uw wachtwoord is hetzelfde als de oude", Toast.LENGTH_SHORT).show();
        } else if (OldPw != null && NewPw != null && PwVerify != null &&  NewPw.equals(PwVerify)) {
            Toast.makeText(getApplicationContext(), "Uw wachtwoord is veranderd", Toast.LENGTH_SHORT).show();
            finish();
        } else if (OldPw != null && NewPw != null && PwVerify != null &&  !NewPw.equals(PwVerify)){
            Toast.makeText(getApplicationContext(), "Uw nieuwe wachtwoorden zijn niet gelijk", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Voer wachtwoorden in", Toast.LENGTH_SHORT).show();
        }
    }
}