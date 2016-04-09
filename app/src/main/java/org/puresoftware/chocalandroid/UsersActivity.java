package org.puresoftware.chocalandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.List;

public class UsersActivity extends AppCompatActivity {

    private static ListView usersView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        // Load users list view
        usersView = (ListView) findViewById(R.id.users_list);
        usersView.setAdapter(new UserAdapter(this));
    }
}
