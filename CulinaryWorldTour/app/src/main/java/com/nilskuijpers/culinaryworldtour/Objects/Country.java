package com.nilskuijpers.culinaryworldtour.Objects;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class Country {

    private String Alpha2Code;
    private String Alpha3Code;
    private String Name;
    private String Capital;
    private int Population;
    private LatLng LatitudeLongitude;
    private Bitmap FlagBitmap;
    private List<Dish> SubmittedDishes;

    public Country(){this.SubmittedDishes = new ArrayList<>();}

    public Country(String alpha2code, String alpha3code, String name, String capital, int population, LatLng latlong)
        {
            this.Alpha2Code = alpha2code;
            this.Alpha3Code = alpha3code;
            this.Name = name;
            this.Capital = capital;
            this.Population = population;
            this.LatitudeLongitude = latlong;
            this.SubmittedDishes = new ArrayList<>();

        }

    public void addDish(Dish d)
    {
        SubmittedDishes.add(d);
    }

    public String getAlpha2Code()
    {
        return this.Alpha2Code;
    }

    public String getAlpha3Code()
    {
        return this.Alpha3Code;
    }

    public String getName()
    {
        return this.Name;
    }

    public String getCapital()
    {
        return this.Capital;
    }

    public int getPopulation()
    {
        return this.Population;
    }

    public LatLng getLocation()
    {
        return this.LatitudeLongitude;
    }

    public Bitmap getCountryFlag()
    {
        return this.FlagBitmap;
    }

    public void setAlpha3Code(String alpha3Code)
    {
        this.Alpha3Code = alpha3Code;
    }

    public void setAlpha2Code(String alpha2Code)
    {
        this.Alpha2Code = alpha2Code;
    }

    public void setName(String name)
    {
        this.Name = name;
    }

    public void setCapital(String capital)
    {
        this.Capital = capital;
    }

    public void setPopulation(int population)
    {
        this.Population = population;
    }

    public void setLocation(Double lat, Double lon)
    {
        this.LatitudeLongitude = new LatLng(lat,lon);
    }

    public void  setCountryFlag(Bitmap flagBitmap)
    {
        this.FlagBitmap = flagBitmap;
    }


}
