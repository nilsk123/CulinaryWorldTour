package com.nilskuijpers.culinaryworldtour.Fragments.SubViews;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nilskuijpers.culinaryworldtour.Objects.Country;
import com.nilskuijpers.culinaryworldtour.R;
import com.nilskuijpers.culinaryworldtour.SharedObjects;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CountryDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CountryDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CountryDetailFragment extends Fragment {

    private SharedObjects data;
    private Country country;
    private TextView countryName;
    private TextView countryCapital;
    private TextView countryPopulation;
    private ImageView countryImageFlag;

    private OnFragmentInteractionListener mListener;

    public CountryDetailFragment() {
        // Required empty public constructor
    }


    public static CountryDetailFragment newInstance() {
        CountryDetailFragment fragment = new CountryDetailFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        data = SharedObjects.getInstance();

        country = data.getChosenCountry();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_country_detail, container, false);

        countryName = (TextView) view.findViewById(R.id.countryNamePlaceholder);
        countryCapital = (TextView) view.findViewById(R.id.countryCapitalPlaceholder);
        countryPopulation = (TextView) view.findViewById(R.id.countryPopPlaceholder);
        countryImageFlag = (ImageView) view.findViewById(R.id.detailViewCountryFlag);

        countryName.setText(country.getName());
        countryCapital.setText(country.getCapital());
        countryPopulation.setText(String.valueOf(country.getPopulation()));
        countryImageFlag.setImageBitmap(country.getCountryFlag());

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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
