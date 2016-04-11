package com.nilskuijpers.culinaryworldtour.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nilskuijpers.culinaryworldtour.Country;
import com.nilskuijpers.culinaryworldtour.R;

/**
 * Created by surfa on 7-4-2016.
 */
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
