package com.nilskuijpers.culinaryworldtour.Fragments.MainViews;


import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.nilskuijpers.culinaryworldtour.Interfaces.TaskDelegate;
import com.nilskuijpers.culinaryworldtour.Misc.AppBarStateChangeListener;
import com.nilskuijpers.culinaryworldtour.Objects.Country;
import com.nilskuijpers.culinaryworldtour.R;
import com.nilskuijpers.culinaryworldtour.SharedObjects;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CountrySelectorFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CountrySelectorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CountrySelectorFragment extends Fragment implements OnMapReadyCallback, TaskDelegate {


    private OnFragmentInteractionListener mListener;
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;
    private Animation fabRotate;
    private LinearLayout rv;
    private FloatingActionButton fab;
    private FloatingActionButton collapsedFab;
    private SupportMapFragment gMapFragment;
    private SharedObjects data;
    private GoogleMap gMap;
    private ImageView countryFlagImageView;
    private CoordinatorLayout root;

    public CountrySelectorFragment() {
        // Required empty public constructor
    }

    public static CountrySelectorFragment newInstance() {
        return new CountrySelectorFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = SharedObjects.getInstance();
        data.initWithContext(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_country_selector, container, false);

        gMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        gMapFragment.getMapAsync(this);

        root = (CoordinatorLayout) view.findViewById(R.id.root_layout);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        fabRotate = AnimationUtils.loadAnimation(getContext(), R.anim.rotate);

        rv = (LinearLayout) view.findViewById(R.id.container);

        appBarLayout = (AppBarLayout) view.findViewById(R.id.appbar);
        collapsedFab = (FloatingActionButton) view.findViewById(R.id.collapsedFab);
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

        countryFlagImageView = (ImageView) view.findViewById(R.id.countryFlagImageView);

        fab = (FloatingActionButton) view.findViewById(R.id.fabRefresh);



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try
                {
                    fabRotate.setRepeatMode(Animation.INFINITE);
                    fab.startAnimation(fabRotate);
                    rv.removeAllViews();
                    data.randomCountry(CountrySelectorFragment.this);
                }

                catch(Exception ex)
                {
                    Log.e("Error", ex.getMessage());
                }
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.gMap = googleMap;
    }

    @Override
    public void TaskCompletionResult(Country c) {
        Log.i("Fragment info", "Delegate received for " + c.getName());
        if(data.getPreviousCountry() != null)
        {
            Snackbar.make(root, c.getName() + " has been loaded", Snackbar.LENGTH_LONG).setAction("Action", null).show();

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

        LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.countryheaderview, rv,true);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
