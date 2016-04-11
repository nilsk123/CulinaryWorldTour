package com.nilskuijpers.culinaryworldtour;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.nilskuijpers.culinaryworldtour.Interfaces.TaskDelegate;
import com.nilskuijpers.culinaryworldtour.Misc.AppBarStateChangeListener;
import com.nilskuijpers.culinaryworldtour.Misc.RVAdapter;
import com.nilskuijpers.culinaryworldtour.Views.CountryHeaderView;

public class MainActivity extends AppCompatActivity implements TaskDelegate {

    private SharedObjects data;
    private FloatingActionButton fab;
    private FloatingActionButton collapsedFab;
    private GoogleMap gMap;
    private ImageView countryFlagImageView;
    private AppBarLayout appBarLayout;
    private Animation fabRotate;
    private LinearLayout rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

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
        data.initWithContext(MainActivity.this);

        gMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

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
                    data.randomCountry();
                }

                catch(Exception ex)
                {
                    Log.e("Error", ex.getMessage().toString());
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    public void TaskCompletionResult(Country c) {
        Log.i("Info", "Delegate received for " + c.getName());

        if(c != null)
        {
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

            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(c.getLocation(), 5));
            fabRotate.setRepeatMode(Animation.ABSOLUTE);
            fabRotate.setRepeatCount(0);
            fabRotate.cancel();
            fab.clearAnimation();



            CountryHeaderView chv = new CountryHeaderView(getApplicationContext(),c);

            LayoutInflater.from(this).inflate(R.layout.countryheaderview,(ViewGroup)findViewById(R.id.container),true);

            /*ImageView iv = (ImageView) findViewById(R.id.person_photo);
            TextView tv = (TextView) findViewById(R.id.person_name);
            TextView tv2 = (TextView) findViewById(R.id.person_age);
            iv.setImageBitmap(c.getCountryFlag());
            tv.setText(c.getName());
            tv2.setText(c.getCapital());*/

        }
    }
}


