package com.resist.movelife;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.LoginButton.UserInfoChangedCallback;
import com.facebook.widget.ProfilePictureView;

public class Login extends FragmentActivity {
	private TextView userName;
	private UiLifecycleHelper uiHelper;
	private TextView accountText;
	private ProfilePictureView profilePictureView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		final LoginButton loginBtn;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
		uiHelper = new UiLifecycleHelper(this, statusCallback);
		uiHelper.onCreate(savedInstanceState);
		userName = (TextView) findViewById(R.id.user_name);
		loginBtn = (LoginButton) findViewById(R.id.fb_login_button);
		accountText = (TextView) findViewById(R.id.accountText);
		profilePictureView = (ProfilePictureView) findViewById(R.id.profilePicture);
		if(!Menu.getUpdater().isConnected()) {
			accountText.setText(getResources().getString(R.string.no_internet));
			loginBtn.setVisibility(View.GONE);
		}
		loginBtn.setUserInfoChangedCallback(new UserInfoChangedCallback() {
			@Override
			public void onUserInfoFetched(GraphUser user) {
				if(user != null) {
					userName.setText(String.format(getResources().getString(R.string.facebook_welcome), user.getName()));
					profilePictureView.setProfileId(user.getId());
					accountText.setVisibility(View.GONE);
					profilePictureView.setVisibility(View.VISIBLE);
					final String userID = user.getId();
					final String userName = user.getName();
					new Thread(new Runnable() {
						public void run() {
							ServerConnection.setFacebook(userID, userName);
						}
					}).start();
				} else {
					userName.setText(getResources().getString(R.string.facebook_not_logged_in));
					profilePictureView.setVisibility(View.GONE);
				}
			}
		});
	}

	private Session.StatusCallback statusCallback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			if(state.isOpened()) {
				//Log.d("FacebookSampleActivity", "Facebook session opened");
			} else if(state.isClosed()) {
				// Log.d("FacebookSampleActivity", "Facebook session closed");
			}
		}
	};

	@Override
	public void onResume() {
		super.onResume();
		uiHelper.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onSaveInstanceState(Bundle savedState) {
		super.onSaveInstanceState(savedState);
		uiHelper.onSaveInstanceState(savedState);
	}
}
