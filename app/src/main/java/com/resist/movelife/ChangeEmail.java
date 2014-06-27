package com.resist.movelife;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ChangeEmail extends Activity {
	private String currentEmail = Menu.getUpdater().getEmail();
	private Button b;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.changeemail);
		overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
		b = (Button) findViewById(R.id.btn_ChangeEmail);
		if(Menu.getUpdater().isConnected()) {
			TextView CurrentEmail = (TextView) findViewById(R.id.tv_CurrentEmail);
			CurrentEmail.setText(currentEmail);
		} else {
			TextView t = (TextView) findViewById(R.id.tv_CurrentEmail);
			b.setVisibility(View.GONE);
			t.setText(getResources().getString(R.string.no_internet));
		}
	}

	public void changeEmail(View v) {
		EditText email = (EditText) findViewById(R.id.et_Email);
		EditText emailVerify = (EditText) findViewById(R.id.et_EmailVerify);
		final String e = email.getText().toString();
		String eVerify = emailVerify.getText().toString();
		if(e != null && eVerify != null && e.equals(eVerify)) {
			if(android.util.Patterns.EMAIL_ADDRESS.matcher(e).matches()) {
				b.setVisibility(View.GONE);
				final ChangeEmail parent = this;
				new Thread(new Runnable() {
					public void run() {
						String msg = getResources().getString(R.string.email_unchanged);
						if(Menu.getUpdater().setUserEmail(e)) {
							msg = getResources().getString(R.string.email_changed);
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
			} else {
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.email_not_correct), Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(getApplicationContext(), getResources().getString(R.string.email_not_the_same), Toast.LENGTH_SHORT).show();
		}
	}
}
