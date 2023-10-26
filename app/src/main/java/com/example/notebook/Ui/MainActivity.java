package com.example.notebook.Ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import com.example.notebook.Entity.Note;
import com.example.notebook.Fragments.ArchivedFragment;
import com.example.notebook.Fragments.FavouritesFragment;
import com.example.notebook.Fragments.HomeFragment;
import com.example.notebook.Fragments.SendData;
import com.example.notebook.Fragments.SettingsFragment;
import com.example.notebook.R;
import com.example.notebook.databinding.ActivityMainBinding;
import com.example.notebook.recyclerview.RecyclerAdapter;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, NavigationView.OnNavigationItemSelectedListener, SendData {

    private ActivityMainBinding binding;
    private SharedPreferences sharedPreferences;
    private SearchView searchView;
    private MenuItem itemSerach;
    boolean checkConfigurationChangesToSearch = false;
    private List<Note> noteList;
    private RecyclerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Notebook);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);

        checkedDarkMode(sharedPreferences.getBoolean(SettingsFragment.NIGHTMODE, false));
        setupBottomNavigationView();
        setupNavigationDrawer();
        checkConfigurationChanges(savedInstanceState);

        clickListenerFloatingactionbutton();


    }


    private void checkConfigurationChanges(Bundle savedInstanceState) {//check Configuration Changes
        if (savedInstanceState == null) {
            replaceFragment(new HomeFragment());

        } else {
            checkConfigurationChangesToSearch = savedInstanceState.getBoolean("Shearch");
        }
    }

    private void checkedDarkMode(boolean nightmode) {//check Dark Mode
        if (nightmode)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }


    private void setupBottomNavigationView() {
        binding.bottomNavigationView.setBackground(null);
        binding.bottomNavigationView.getMenu().getItem(1).setEnabled(false);//Disable the second item To create empty space in the Bottom Navigation Bar
        binding.bottomNavigationView.setOnItemSelectedListener(this);
    }


    private void setupNavigationDrawer() {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("");
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawerlayout, binding.toolbar, R.string.Navigation_Drawer_Open, R.string.Navigation_Drawer_close);
        binding.drawerlayout.addDrawerListener(toggle);
        toggle.syncState();
        binding.NavigationView.setNavigationItemSelectedListener(this);
    }


    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.home_frameLayout, fragment);
        fragmentTransaction.commit();


    }


    public void SearchView_SetOnQueryTextListener() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                filter(newText, noteList);

                return true;
            }
        });

    }

    //This function performs a search in the list according to the words written in the SearchView
    //
    public void filter(String text, List<Note> noteList) {
        List<Note> filterlist = new ArrayList<>();
        text.toLowerCase();
        for (Note note : noteList) {
            if (note.getTitle().toLowerCase().contains(text) || note.getDescription().toLowerCase().contains(text)) {
                filterlist.add(note);
            }
        }


        adapter.setList(filterlist);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.Home) {

            itemSerach.setVisible(true);
            binding.NavigationView.getMenu().getItem(0).setChecked(true);
            binding.bottomNavigationView.getMenu().getItem(0).setChecked(true);
            replaceFragment(new HomeFragment());


        } else if (item.getItemId() == R.id.Settings) {

            itemSerach.collapseActionView();
            itemSerach.setVisible(false);

            binding.NavigationView.getMenu().getItem(0).setChecked(false);
            binding.NavigationView.getMenu().getItem(1).setChecked(false);
            binding.NavigationView.getMenu().getItem(2).setChecked(false);

            replaceFragment(new SettingsFragment());

        } else if (item.getItemId() == R.id.NavigationDrawer_Favourites) {

            itemSerach.setVisible(true);
            replaceFragment(new FavouritesFragment());
            binding.bottomNavigationView.getMenu().getItem(0).setChecked(true);

        } else if (item.getItemId() == R.id.NavigationDrawer_Archived) {
            itemSerach.setVisible(true);
            replaceFragment(new ArchivedFragment());
            binding.bottomNavigationView.getMenu().getItem(0).setChecked(true);
        }

        binding.drawerlayout.closeDrawer(GravityCompat.START);//To hide the NavigationDrawer after selection.
        return true;
    }

    private void clickListenerFloatingactionbutton() {
        binding.floatingactionbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), NoteActivity.class);
                startActivity(intent);
            }
        });

    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {

        super.onSaveInstanceState(outState);

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.home_frameLayout);

        if (currentFragment != null) {

            outState.putBoolean("Shearch", true ? currentFragment instanceof SettingsFragment : false);

            getSupportFragmentManager().putFragment(outState, "lastFragment", currentFragment);
        }

    }


    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);

        Fragment lastFragment = getSupportFragmentManager().findFragmentByTag("lastFragment");
        if (lastFragment != null) {

            replaceFragment(lastFragment);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        itemSerach = menu.findItem(R.id.Search_menu);
        searchView = (SearchView) itemSerach.getActionView();
        searchView.setQueryHint(getString(R.string.main_Search));

        if (checkConfigurationChangesToSearch) itemSerach.setVisible(false);

        SearchView_SetOnQueryTextListener();
        return super.onCreateOptionsMenu(menu);
    }





    @Override
    public void sendAdapter(RecyclerAdapter adapter) {//listener to send To send Adapter from fragment to activity
        this.adapter = adapter;
        this.noteList = adapter.getList();
    }
}