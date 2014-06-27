package com.resist.movelife;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Thomas on 26-6-2014.
 */
public class VriendToevoegen extends Activity{
    private Button btn;
    private EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vriendtoevoegen);
        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
        btn=(Button)findViewById(R.id.btn_vriendtoevoegen);
        editText = (EditText) findViewById(R.id.et_vriendtoevoegen);
        final VriendToevoegen parent = this;



        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                final String email = editText.getText().toString();
                if(android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                    new Thread(new Runnable() {
                        public void run() {
                            ServerConnection.addFriend(email);

                            parent.runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(parent.getBaseContext(), parent.getResources().getString(R.string.friendrequest_sent), Toast.LENGTH_LONG).show();
                                    parent.finish();
                                    btn.setVisibility(View.GONE);
                                }
                            });
                        }
                    }).start();
                }else {
                    Toast.makeText(getApplicationContext(), parent.getResources().getString(R.string.email_not_correct), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
