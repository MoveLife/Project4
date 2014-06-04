package example.myapplication.movelife;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.LoginButton.UserInfoChangedCallback;
import com.facebook.widget.ProfilePictureView;

public class Login extends FragmentActivity {

    private LoginButton loginBtn;
    private Button Login_btn_Login;
    private Button Login_btn_createAccount;
    private TextView userName;
    private UiLifecycleHelper uiHelper;
    private ProfilePictureView profilePicture;
    private EditText loginEmail;
    private EditText loginPassword;
    private TextView accountText;
    private ProfilePictureView profilePictureView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);

        uiHelper = new UiLifecycleHelper(this, statusCallback);
        uiHelper.onCreate(savedInstanceState);
        userName = (TextView) findViewById(R.id.user_name);
        loginBtn = (LoginButton) findViewById(R.id.fb_login_button);
        loginEmail = (EditText)findViewById(R.id.Login_Email);
        loginPassword = (EditText)findViewById(R.id.Login_Password);
        Login_btn_Login = (Button)findViewById(R.id.Login_btn_Login);
        accountText = (TextView)findViewById(R.id.accountText);
        Login_btn_createAccount = (Button)findViewById(R.id.Login_btn_createAccount);
        profilePictureView = (ProfilePictureView) findViewById(R.id.profilePicture);

        loginBtn.setUserInfoChangedCallback(new UserInfoChangedCallback() {
            @Override
            public void onUserInfoFetched(GraphUser user) {
                if (user != null) {
                    userName.setText("Hello, " + user.getName());
                    profilePictureView.setProfileId(user.getId());
                    loginEmail.setVisibility(View.GONE);
                    loginPassword.setVisibility(View.GONE);
                    Login_btn_Login.setVisibility(View.GONE);
                    accountText.setVisibility(View.GONE);
                    Login_btn_createAccount.setVisibility(View.GONE);
                } else {
                    userName.setText("You are not logged in");
                    profilePictureView.setVisibility(View.GONE);
                }
            }
        });
    }

    private Session.StatusCallback statusCallback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state,
                         Exception exception) {
            if (state.isOpened()) {

                Log.d("FacebookSampleActivity", "Facebook session opened");
            } else if (state.isClosed()) {

                Log.d("FacebookSampleActivity", "Facebook session closed");
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