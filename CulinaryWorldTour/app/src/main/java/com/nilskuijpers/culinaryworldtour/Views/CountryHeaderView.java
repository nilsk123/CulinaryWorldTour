package com.nilskuijpers.culinaryworldtour.Views;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nilskuijpers.culinaryworldtour.Objects.Country;
import com.nilskuijpers.culinaryworldtour.R;

public class CountryHeaderView extends LinearLayout {


    public CountryHeaderView(Context context, Country c)
    {
        super(context);

        LayoutInflater inflater = LayoutInflater.from(context);

        LinearLayout content = (LinearLayout) inflater.inflate(R.layout.countryheaderview, CountryHeaderView.this);

        TextView countryName = (TextView) content.findViewById(R.id.countryNamePlaceholder);

        countryName.setText(c.getName());
    }
}
