package com.digitalgarden.gameswap.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import com.digitalgarden.gameswap.R;
import com.digitalgarden.gameswap.adapter.AdapterGridCategory;
import com.digitalgarden.gameswap.model.Category;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActivityHome extends Activity_Base implements NavigationView.OnNavigationItemSelectedListener {

    private static final int RC_SIGN_IN = 123;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupFloatingActionButton();
        setupNavigationDrawer();
        setupFirebase();
        setupGridview();
    }

    private void setupFloatingActionButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
    }

    private void setupNavigationDrawer() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    private void setupFirebase() {
        mAuth = FirebaseAuth.getInstance();
    }

    private void setupGridview() {
        GridView gridView = (GridView) findViewById(R.id.activity_gridview);
        List<Category> strings = new ArrayList<>();
        strings.add(new Category("PS4", R.drawable.console_ps4));
        strings.add(new Category("Xbox One", R.drawable.console_xboxone));
        strings.add(new Category("Switch", R.drawable.console_switch));
        strings.add(new Category("PS3", R.drawable.console_ps3));
        strings.add(new Category("Xbox 360", R.drawable.console_xbox360));
        strings.add(new Category("Wii U", R.drawable.console_wiiu));
        strings.add(new Category("PS2", R.drawable.console_ps2));
        strings.add(new Category("Wii", R.drawable.console_wii));

        AdapterGridCategory adapter = new AdapterGridCategory(getContext(), strings);
        gridView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.splash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
        else if (id == R.id.nav_my_posts) {
            // 1. Instantiate an AlertDialog.Builder with its constructor
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

            // 2. Chain together various setter methods to set the dialog characteristics
            builder.setTitle("My Posts")
                    .setMessage("Please add a Gameswap account to view your posts, do you want to add one now?")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // FIRE ZE MISSILES!
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });

            // 3. Get the AlertDialog from create()
            AlertDialog dialog = builder.create();
            dialog.show();

            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
        else if (id == R.id.nav_create_post) {
            // 1. Instantiate an AlertDialog.Builder with its constructor
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

            // 2. Chain together various setter methods to set the dialog characteristics
            builder.setTitle("Create Post")
                    .setMessage("Creating a post requires a user account, would you like to create a user account now?")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent i = new Intent(ActivityHome.this, ActivityCreatePost.class);
                            startActivity(i);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });

            // 3. Get the AlertDialog from create()
            AlertDialog dialog = builder.create();
            dialog.show();

            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
        else if (id == R.id.nav_locations) {
            Intent i = new Intent(ActivityHome.this, ActivityLocations.class);
            startActivity(i);

            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
        else if (id == R.id.nav_settings) {
            Intent i = new Intent(ActivityHome.this, ActivitySettings.class);
            startActivity(i);

            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
        else if (id == R.id.nav_about) {
            Intent i = new Intent(ActivityHome.this, ActivityAbout.class);
            startActivity(i);

            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
        else if (id == R.id.nav_create_account) {
            Intent i = new Intent(ActivityHome.this, ActivityCreateAccount.class);
            startActivity(i);

            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
        else if (id == R.id.nav_sign_in) {
            // Choose authentication providers
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build());
                    //new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build(),

            // Create and launch sign-in intent
            startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build(),
                RC_SIGN_IN);

            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
        else if (id == R.id.nav_product_page) {
            Intent i = new Intent(ActivityHome.this, ActivityProductList.class);
            startActivity(i);

            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
        else {
            return false;
        }
    }
}
