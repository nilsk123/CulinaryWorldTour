package com.nilskuijpers.culinaryworldtour;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

import com.nilskuijpers.culinaryworldtour.DatabaseLogic.CountriesDataSource;
import com.nilskuijpers.culinaryworldtour.Interfaces.TaskDelegate;
import com.nilskuijpers.culinaryworldtour.NetworkLogic.CountryParser;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;


/**
 * Created by surfa on 30-3-2016.
 */
public class SharedObjects {
    private static SharedObjects ourInstance = new SharedObjects();

    private CountriesDataSource countriesDataSource;
    private CountryParser countryParser;
    private Country previousCountry;
    private Country chosenCountry;
    private List<Country> allCountries;
    private Context context;
    private String[] alpha3Array;


    public static SharedObjects getInstance() {
        return ourInstance;
    }

    public void randomCountry(TaskDelegate activityContext) throws ExecutionException, InterruptedException {
        alpha3Array = context.getResources().getStringArray(R.array.iso3166);
        this.setChosenCountry(alpha3Array[new Random().nextInt(alpha3Array.length)],activityContext);
    }

    public List<Country> getAllCountries()
    {
        return this.allCountries;
    }

    private SharedObjects() {
    }

    public void initWithContext(Context cntxt)
    {
        this.context = cntxt;
        countriesDataSource = new CountriesDataSource(cntxt);
        countriesDataSource.open();
        this.refreshCountryList();
    }

    public void mockDish()
    {
        countriesDataSource.storeDish(new Dish(null, "Sjapsjoi", "Met zoete saus :D", null, this.chosenCountry));
    }

    public void setChosenCountry(Country c)
    {
        this.allCountries.add(c);
        this.chosenCountry = c;
    }

    public void setChosenCountry(String alpha3code, TaskDelegate activityContext) throws ExecutionException, InterruptedException {

        for (Country c : allCountries)
        {
            if(c.getAlpha3Code().toLowerCase().equals(alpha3code.toLowerCase()))
            {
                TaskDelegate td = activityContext;
                this.previousCountry = this.chosenCountry;
                this.chosenCountry = c;
                td.TaskCompletionResult(c);
                Log.d("Debug",this.chosenCountry.getName() + " found in local database");
                break;
            }
        }

        if(this.chosenCountry == null || !this.chosenCountry.getAlpha3Code().toLowerCase().equals(alpha3code.toLowerCase()))
        {
            countryParser = new CountryParser((MainActivity) activityContext);
            this.previousCountry = this.chosenCountry;
            this.countryParser.execute(alpha3code);
        }
    }

    public Country getChosenCountry()
    {
        return this.chosenCountry;
    }

    public Country getPreviousCountry()
    {
        return this.previousCountry;
    }

    public CountryParser getCountryParser()
    {
        return this.countryParser;
    }

    public void refreshCountryList()
    {
        allCountries = countriesDataSource.getAllCountries();
    }
}
