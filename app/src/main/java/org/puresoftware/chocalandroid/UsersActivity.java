package org.puresoftware.chocalandroid;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.List;

public class UsersActivity extends AppCompatActivity {

    private static ListView usersView;
    private UserAdapter mAdapter = new UserAdapter(this);

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
        usersView = (ListView) findViewById(R.id.users_list);
        usersView.setAdapter(mAdapter);
    }

    public void refreshOnlineUsers() {
        mAdapter.notifyDataSetChanged();
    }
}
