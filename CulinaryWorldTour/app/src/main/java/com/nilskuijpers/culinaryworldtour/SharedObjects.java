package com.nilskuijpers.culinaryworldtour;

import android.content.Context;
import android.util.Log;

import com.nilskuijpers.culinaryworldtour.DatabaseLogic.CountriesDataSource;
import com.nilskuijpers.culinaryworldtour.Interfaces.TaskDelegate;
import com.nilskuijpers.culinaryworldtour.NetworkLogic.CountryParser;
import com.nilskuijpers.culinaryworldtour.Objects.Country;
import com.nilskuijpers.culinaryworldtour.Objects.Dish;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;


public class SharedObjects {
    private static SharedObjects ourInstance = new SharedObjects();

    private CountriesDataSource countriesDataSource;
    private CountryParser countryParser;
    private Country previousCountry;
    private Country chosenCountry;
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
        return countriesDataSource.getAllCountries();
    }

    private SharedObjects() {
    }

    public void initWithContext(Context cntxt)
    {
        this.context = cntxt;
        countriesDataSource = new CountriesDataSource(cntxt);
        countriesDataSource.open();
    }

    public void mockDish()
    {
        countriesDataSource.storeDish(new Dish(null, "Sjapsjoi", "Met zoete saus :D", null, this.chosenCountry));
    }

    public void setChosenCountry(Country c)
    {
        this.chosenCountry = c;
    }

    public void setChosenCountry(String alpha3code, TaskDelegate activityContext) throws ExecutionException, InterruptedException {

        Country c = countriesDataSource.getCountry(alpha3code);

        if(c != null)
        {
            this.previousCountry = this.chosenCountry;
            this.chosenCountry = c;
            activityContext.TaskCompletionResult(c);
            Log.d("Debug",this.chosenCountry.getName() + " found in local database");
        }

        else
        {
            countryParser = new CountryParser(this.context, activityContext);
            this.previousCountry = this.chosenCountry;
            this.countryParser.execute(alpha3code);
        }
    }

    public Country getPreviousCountry()
    {
        return this.previousCountry;
    }
}
