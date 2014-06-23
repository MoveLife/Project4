package com.resist.movelife;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.resist.movelife.R;

import java.util.List;

public class FriendRequest extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friendrequest);
    }

    public static void setUIDs(List<Integer> uids) {

    }
}
