package com.nilskuijpers.culinaryworldtour.Misc;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.nilskuijpers.culinaryworldtour.Country;
import com.nilskuijpers.culinaryworldtour.R;

import java.util.List;

/**
 * Created by surfa on 7-4-2016.
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.CountryViewHolder> {

    List<Country> countries;

    public RVAdapter(List<Country> Countries){
        this.countries = Countries;
    }


    @Override
    public RVAdapter.CountryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        CountryViewHolder pvh = new CountryViewHolder(v);
        return pvh;

    }

    @Override
    public void onBindViewHolder(CountryViewHolder holder, int position) {
        holder.personName.setText(countries.get(position).getName());
        holder.personAge.setText(String.valueOf(countries.get(position).getPopulation()));
        holder.personPhoto.setImageBitmap(countries.get(position).getCountryFlag());

    }

    @Override
    public int getItemCount() {
        Log.d("Debugsize", String.valueOf(countries.size()));
        return countries.size();

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }



    public static class CountryViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView personName;
        TextView personAge;
        ImageView personPhoto;

        CountryViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            personName = (TextView)itemView.findViewById(R.id.person_name);
            personAge = (TextView)itemView.findViewById(R.id.person_age);
            personPhoto = (ImageView)itemView.findViewById(R.id.person_photo);
        }
    }
}
