package com.resist.movelife;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ChangePassword extends Activity{
    private Button btn_ChangePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changepassword);
        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
        btn_ChangePassword = (Button) findViewById(R.id.btn_ChangePassword);

        if(!Menu.getUpdater().isConnected()) {
            TextView noInternet = (TextView)findViewById(R.id.tv_noInternet);
            noInternet.setVisibility(View.VISIBLE);
            noInternet.setText("U heeft geen internetverbinding");
            btn_ChangePassword.setVisibility(View.GONE);
        } else if(Menu.getUpdater().getUserSetPassword() == null) {
            EditText oldPassword = (EditText)findViewById(R.id.et_OldPassword);
            oldPassword.setVisibility(View.GONE);
        }
    }

    public void checkPassword(View v) {
        EditText oldPassword = (EditText)findViewById(R.id.et_OldPassword);
        EditText newPassword = (EditText)findViewById(R.id.et_NewPassword);
        EditText passwordVerify = (EditText)findViewById(R.id.et_PasswordVerify);

        String opw = oldPassword.getText().toString();
        if(oldPassword.getVisibility() == View.GONE || opw.isEmpty()) {
            opw = null;
        }
        final String oldPw = opw;
        final String newPw = newPassword.getText().toString();
        String pwVerify = passwordVerify.getText().toString();

        if(newPw == null || pwVerify == null || newPw.equals(oldPw)) {
            Toast.makeText(getApplicationContext(), "Voer wachtwoorden in", Toast.LENGTH_SHORT).show();
        } else {
            if (!newPw.equals(pwVerify)) {
                Toast.makeText(getApplicationContext(), "Uw nieuwe wachtwoorden zijn niet gelijk", Toast.LENGTH_SHORT).show();
            } else {
                btn_ChangePassword.setVisibility(View.GONE);
                final ChangePassword parent = this;
                new Thread(new Runnable() {
                    public void run() {
                        String msg = "Uw wachtwoord is niet veranderd";
                        if (Menu.getUpdater().setUserPassword(newPw, oldPw)) {
                            msg = "Uw wachtwoord is veranderd";
                        }
                        final String message = msg;
                        parent.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(parent.getBaseContext(), message, Toast.LENGTH_SHORT).show();
                                parent.finish();
                            }
                        });
                    }
                }).start();
            }
        }
    }
}