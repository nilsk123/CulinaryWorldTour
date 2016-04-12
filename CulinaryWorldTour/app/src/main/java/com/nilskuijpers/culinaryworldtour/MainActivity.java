package com.nilskuijpers.culinaryworldtour;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.nilskuijpers.culinaryworldtour.Fragments.CountrySelectorFragment;

public class MainActivity extends AppCompatActivity implements CountrySelectorFragment.OnFragmentInteractionListener {

    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private Fragment fragment;
    private FragmentManager fm;
    private FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, mDrawer, R.string.drawer_open, R.string.drawer_close)
            {
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };


        mDrawer.addDrawerListener(mDrawerToggle);

        setupDrawerContent((NavigationView) findViewById(R.id.nvView));

        setHomeFragment();
    }

    private void setHomeFragment()
    {
        fragment = CountrySelectorFragment.newInstance();

        ft = getSupportFragmentManager().beginTransaction();

        ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);

        ft.replace(R.id.flContent,fragment);

        ft.commit();

    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked

        Class fragmentClass;
        switch(menuItem.getItemId()) {
            case R.id.country_selector_fragment:
                fragmentClass = CountrySelectorFragment.class;
                break;
            default:
                fragmentClass = CountrySelectorFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment

        ft = getSupportFragmentManager().beginTransaction();

        ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);

        ft.replace(R.id.flContent,fragment);

        ft.commit();

        // Highlight the selected item has been done by NavigationView

        menuItem.setChecked(true);

        // Close the navigation drawer

        mDrawer.closeDrawers();
    }





    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }


}


