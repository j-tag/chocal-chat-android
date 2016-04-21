package org.puresoftware.chocalandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * org.puresoftware.chocalandroid
 * Created by Hesam Gholami on 2016/03/22 18:56.
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        refreshTitle();

        // Set current Chocal activity
        Chocal.setActivity(MainActivity.this);

        // Set name and status in navigation pane header
        View headerView = navigationView.getHeaderView(0);
        TextView nameView = (TextView) headerView.findViewById(R.id.nav_name);
        TextView statusView = (TextView) headerView.findViewById(R.id.nav_status);

        nameView.setText(Chocal.getCurentUser().name);
        statusView.setText(R.string.online);

        // Show user Avatar as a circular image
        ImageView avatar = (ImageView) headerView.findViewById(R.id.nav_avatar);
        ImageView chatAvatar = (ImageView) findViewById(R.id.img_avatar);
        avatar.setImageDrawable(Chocal.getCurentUser().getAvatarDrawable(this));
        chatAvatar.setImageDrawable(Chocal.getCurentUser().getAvatarDrawable(this));

        // Handle send button
        Button btnSend = (Button) findViewById(R.id.btn_send);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            // Leave chat
            leave();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_online_users) {
            // Show users list
            Intent intent = new Intent(this, UsersActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_leave_chat) {
            // Leave chat
            leave();
        } else if (id == R.id.nav_about) {
            // Show about page
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void refreshTitle() {
        // Show number of online users as title
        String title = getString(R.string.online_number);
        setTitle(String.format(title, Chocal.getUsers().size()));
    }

    /**
     * This method will triggered when send button is clicked.
     */
    private void send() {
        // TODO : Choose to send plain message or image message
        EditText txtMessage = (EditText) findViewById(R.id.txt_message);
        String strMessage = txtMessage.getText().toString();

        Chocal.sendTextMessage(strMessage);

        // Empty edit text
        txtMessage.setText("");
        txtMessage.requestFocus();
    }

    private void leave() {
        // Disconnect from Chocal Server and leave chat
        Chocal.leave();
        // Show toast to note user that he left the chat
        Toast.makeText(this, R.string.you_left_chat_successfully, Toast.LENGTH_LONG).show();
        // Close current intent
        finish();
        // Show join form
        Intent intent = new Intent(this, JoinActivity.class);
        startActivity(intent);
    }
}
