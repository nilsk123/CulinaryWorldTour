package com.nilskuijpers.culinaryworldtour.Objects;

import android.graphics.Bitmap;
import android.media.Rating;

import java.util.Calendar;
import java.util.Date;

public class Dish {

    private Country country;
    private Date date;
    private Rating rating;
    private String name;
    private String description;
    private Bitmap photo;


    public Dish(){}

    public Dish(Date oldDate, String Name, String Description, Bitmap Photo, Country assCountry)
    {
        if(oldDate == null)
        {
            date = Calendar.getInstance().getTime();
        }
        else
        {
            date = oldDate;
        }

        this.name = Name;
        this.description = Description;

        if(Photo != null)
        {
            this.photo = Photo;
        }

        this.country = assCountry;
    }

    public void setCountry(Country c)
    {
        this.country = c;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public void setDate(Date d)
    {
        this.date = d;
    }

    public void setRating(Float percentage)
    {
        this.rating = null;
    }

    public void setBitmapLocation(String location)
    {
        this.photo = null;
    }

    public String getCountryAlpha3()
    {
        return this.country.getAlpha3Code();
    }

    public String getDate()
    {
        return this.date.toString();
    }

    public String getName()
    {
        return this.name;
    }

    public String getDescription()
    {
        return this.description;
    }

    public Float getRating()
    {
        return Float.valueOf(3);
    }
}
