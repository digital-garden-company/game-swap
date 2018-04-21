package com.digitalgarden.gameswap.activity;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.digitalgarden.gameswap.R;
import com.digitalgarden.gameswap.adapter.AdapterGridCategory;
import com.digitalgarden.gameswap.adapter.AdapterGridPost;
import com.digitalgarden.gameswap.model.Category;
import com.digitalgarden.gameswap.model.Post;
import com.digitalgarden.gameswap.toolbox.Toolbox;
import com.digitalgarden.gameswap.view.ProgressDialogGeneric;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActivityHome extends Activity_Base implements NavigationView.OnNavigationItemSelectedListener {

    static final int REQUESTCODE_REFRESH_SCREEN = 1001;  // The request code

    private static final int RC_SIGN_IN = 123;

    // Access a Cloud Firestore instance from your Activity
    private FirebaseFirestore firestore;
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
        getPosts();
    }

    private void setupFloatingActionButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                Intent i = new Intent(ActivityHome.this, ActivitySearch.class);
                startActivity(i);
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
        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    private void getPosts() {
        final ProgressDialogGeneric dialog = new ProgressDialogGeneric(getContext());
        dialog.show();

        firestore.collection("posts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Toolbox.log(TAG, "getMyPosts() - onComplete()");
                        dialog.dismiss();

                        if (task.isSuccessful()) {
                            List<Post> posts = new ArrayList<>();
                            for (DocumentSnapshot document : task.getResult()) {
                                //Toolbox.log(TAG, document.getId() + " => " + document.getData());
                                Post post = document.toObject(Post.class);
                                //Toolbox.log(TAG, "post: " + post);
                                posts.add(post);
                            }

                            setupGridview(posts);
                        } else {
                            Toolbox.warn(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }

    private void setupGridview(List<Post> posts) {
        GridView gridView = (GridView) findViewById(R.id.activity_gridview);
        final AdapterGridPost adapter = new AdapterGridPost(getContext(), posts);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(getContext(), ActivityMyPost.class);
                intent.putExtra(ActivityMyPost.KEY_POST, adapter.getItem(position));
                startActivity(intent);
            }
        });
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
        getMenuInflater().inflate(R.menu.menu_home, menu);

        // Retrieve the SearchView and plug it into SearchManager
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        EditText searchPlate = (EditText) searchView.findViewById(R.id.search_src_text);
        searchPlate.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Toolbox.hideKeyboard(getActivity());
                    Intent i = new Intent(ActivityHome.this, ActivityProductList.class);
                    startActivity(i);
                }
                return false;

            }
        });

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Toolbox.log(TAG, "onActivityResult - 1");
        if (requestCode == REQUESTCODE_REFRESH_SCREEN) {
            Toolbox.log(TAG, "onActivityResult - 2");
            if (resultCode == RESULT_OK) {
                Toolbox.log(TAG, "onActivityResult - 3");
                getPosts();
            }
        }
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
            if(isSignedIn()) {
                Intent intent = new Intent(ActivityHome.this, ActivityMyPosts.class);
                startActivity(intent);
            }
            else {
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
            }

            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
        else if (id == R.id.nav_create_post) {
            if(isSignedIn()) {
                Intent intent = new Intent(ActivityHome.this, ActivityCreatePost.class);
                startActivityForResult(intent, REQUESTCODE_REFRESH_SCREEN);
            }
            else {
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
            }

            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
        else if (id == R.id.nav_locations) {
            Intent intent = new Intent(ActivityHome.this, ActivityLocations.class);
            startActivity(intent);

            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
        else if (id == R.id.nav_settings) {
            Intent intent = new Intent(ActivityHome.this, ActivitySettings.class);
            startActivity(intent);

            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
        else if (id == R.id.nav_about) {
            Intent intent = new Intent(ActivityHome.this, ActivityAbout.class);
            startActivity(intent);

            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
        else if (id == R.id.nav_create_account) {
            Intent intent = new Intent(ActivityHome.this, ActivityCreateAccount.class);
            startActivity(intent);

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
        else if (id == R.id.nav_sign_out) {
            FirebaseAuth.getInstance().signOut();

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

    private boolean isSignedIn() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            return true;
        } else {
            // No user is signed in
            return false;
        }
    }
}
