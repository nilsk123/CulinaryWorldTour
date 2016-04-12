package com.nilskuijpers.culinaryworldtour;

import android.content.res.Configuration;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.nilskuijpers.culinaryworldtour.Interfaces.TaskDelegate;
import com.nilskuijpers.culinaryworldtour.Misc.AppBarStateChangeListener;

public class MainActivity extends AppCompatActivity implements TaskDelegate, OnMapReadyCallback {

    private SharedObjects data;
    private FloatingActionButton fab;
    private FloatingActionButton collapsedFab;
    private MapFragment gMapFragment;
    private GoogleMap gMap;
    private ImageView countryFlagImageView;
    private AppBarLayout appBarLayout;
    private Animation fabRotate;
    private LinearLayout rv;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        mDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, mDrawer, R.string.drawer_open, R.string.drawer_close){

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                //getActionBar().setTitle(mTitle);
                //invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //getActionBar().setTitle(mDrawerTitle);
                //invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };


        mDrawer.addDrawerListener(mDrawerToggle);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fabRotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);

        rv = (LinearLayout) findViewById(R.id.container);

        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        collapsedFab = (FloatingActionButton) findViewById(R.id.collapsedFab);
        collapsedFab.hide();
        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if(state == State.FADEIN)
                {
                    Log.i("Info", "Fade in fab");

                    collapsedFab.show();

                }
                else if(state == State.FADEOUT)
                {
                    Log.i("Info", "Fade out fab");
                    collapsedFab.hide();

                }
            }
        });

        data = SharedObjects.getInstance();
        data.initWithContext(getApplicationContext());

        //gMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        gMapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        gMapFragment.getMapAsync(this);

        countryFlagImageView = (ImageView) findViewById(R.id.countryFlagImageView);

        fab = (FloatingActionButton) findViewById(R.id.fabRefresh);



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try
                {
                    fabRotate.setRepeatMode(Animation.INFINITE);
                    fab.startAnimation(fabRotate);
                    rv.removeAllViews();
                    data.randomCountry(MainActivity.this);
                }

                catch(Exception ex)
                {
                    Log.e("Error", ex.getMessage());
                }
            }
        });
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
    public void TaskCompletionResult(Country c) {
        Log.i("Info", "Delegate received for " + c.getName());
            if(data.getPreviousCountry() != null)
            {
                Snackbar.make(findViewById(R.id.root_layout), c.getName() + " has been loaded", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                if(data.getPreviousCountry().getCountryFlag() != null) {

                    Drawable[] layers = new Drawable[2];
                    layers[0] = new BitmapDrawable(getResources(), data.getPreviousCountry().getCountryFlag());
                    layers[1] = new BitmapDrawable(getResources(), c.getCountryFlag());

                    TransitionDrawable transitionDrawable = new TransitionDrawable(layers);
                    countryFlagImageView.setImageDrawable(transitionDrawable);
                    transitionDrawable.startTransition(1500);
                }

                else {
                    countryFlagImageView.setImageBitmap(c.getCountryFlag());
                }
            }

            else
            {
                Animation fadeIn = new AlphaAnimation(0, 1);
                fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
                fadeIn.setDuration(1500);

                countryFlagImageView.setImageBitmap(c.getCountryFlag());

                countryFlagImageView.setAnimation(fadeIn);

                fadeIn.start();
            }

            this.gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(c.getLocation(), 5));
            fab.clearAnimation();

            LayoutInflater.from(this).inflate(R.layout.countryheaderview,(ViewGroup)findViewById(R.id.container),true);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.gMap = googleMap;
    }
}


