package app.muneef.itnewsapp.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import app.muneef.itnewsapp.R;
import app.muneef.itnewsapp.fragments.BooksListFragment;
import app.muneef.itnewsapp.fragments.FavouritesFragment;
import app.muneef.itnewsapp.fragments.NewsFragment;
import app.muneef.itnewsapp.preferences.PreferenceManager;

public class DashboardActivity extends AppCompatActivity {


    FirebaseAuth auth;
    PreferenceManager preferenceManager;
    FirebaseDatabase firebaseDatabase;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    NewsFragment newsFragment = new NewsFragment();
                    getSupportActionBar().setTitle("News Feed");
                    loadFragment(DashboardActivity.this,newsFragment);
                    return true;
                case R.id.navigation_books:
                    BooksListFragment booksListFragment = new BooksListFragment();
                    getSupportActionBar().setTitle("Books");
                    loadFragment(DashboardActivity.this,booksListFragment);

                    return true;
//                case R.id.navigation_favourites:
//                    FavouritesFragment favouritesFragment = new FavouritesFragment();
//                    getSupportActionBar().setTitle("Favourites");
//                    loadFragment(DashboardActivity.this,favouritesFragment);
//
//                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        auth = FirebaseAuth.getInstance();
        firebaseDatabase  = FirebaseDatabase.getInstance();



        NewsFragment newsFragment = new NewsFragment();
        getSupportActionBar().setTitle("News Feed");
        loadFragment(DashboardActivity.this,newsFragment);

        preferenceManager = new PreferenceManager(this);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_profile: {

                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            }
            case R.id.menu_logout: {
                logOutConfirmation();
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }


    }

    private void logOutConfirmation() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Logout")
                .setMessage("You will be logout")
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        FirebaseAuth.getInstance().signOut();
                        preferenceManager.logoutUser(DashboardActivity.this);

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.cancel();
            }
        }).show();
    }

    private void loadFragment(FragmentActivity fragmentActivity, Fragment fragment){

        fragmentActivity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container,fragment)
                .commit();

    }

    private void hideButtonFunction(){


    }


}
