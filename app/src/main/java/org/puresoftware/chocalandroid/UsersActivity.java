package org.puresoftware.chocalandroid;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class UsersActivity extends AppCompatActivity {

    private static ListView mUsersView;
    private UserAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        // Set current chocal activity
        Chocal.setActivity(this);

        // Enable back button
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Load users list view
        mAdapter = new UserAdapter(this);
        mUsersView = (ListView) findViewById(R.id.users_list);
        mUsersView.setAdapter(mAdapter);
    }

    public void refreshOnlineUsers() {
        UsersActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                mAdapter.notifyDataSetChanged();
            }
        });
    }
}
