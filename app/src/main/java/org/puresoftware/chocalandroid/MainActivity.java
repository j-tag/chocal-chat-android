package org.puresoftware.chocalandroid;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

        // Set current Chocal activity
        Chocal.setActivity(MainActivity.this);

        // Set name and status in navigation pane header
        View headerView = navigationView.getHeaderView(0);
        TextView nameView = (TextView) headerView.findViewById(R.id.nav_name);
        TextView statusView = (TextView) headerView.findViewById(R.id.nav_status);

        nameView.setText(Chocal.getName());
        statusView.setText(R.string.online);

        // Show user Avatar as a circular image
        Bitmap bitmap = Chocal.getAvatar();
        ImageView avatar = (ImageView) headerView.findViewById(R.id.nav_avatar);
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.no_avatar);
        }
        RoundedBitmapDrawable avatarRounded = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        avatarRounded.setCircular(true);
        avatar.setImageDrawable(avatarRounded);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            Chocal.disconnect();
            finish();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_leave_chat) {
            // TODO: Handle the leave action
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
