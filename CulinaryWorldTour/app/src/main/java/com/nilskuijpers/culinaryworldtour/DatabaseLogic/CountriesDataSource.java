package com.nilskuijpers.culinaryworldtour.DatabaseLogic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.nilskuijpers.culinaryworldtour.Country;
import com.nilskuijpers.culinaryworldtour.Dish;
import com.nilskuijpers.culinaryworldtour.FilesystemLogic.ImageSaver;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by surfa on 30-3-2016.
 */
public class CountriesDataSource {

    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private Context context;


    private String [] allCountryColumns = { dbHelper.COUNTRY_COLUMN_ALPHA3, dbHelper.COUNTRY_COLUMN_ALPHA2, dbHelper.COUNTRY_COLUMN_NAME, dbHelper.COUNTRY_COLUMN_CAPITAL, dbHelper.COUNTRY_COLUMN_POPULATION, dbHelper.COUNTRY_COLUMN_LAT, dbHelper.COUNTRY_COLUMN_LONG, dbHelper.COUNTRY_COLUMN_BITMAPLOCATION};
    private String [] allDishColumns = { dbHelper.DISH_COLUMN_ALPHA3, dbHelper.DISH_COLUMN_DATE, dbHelper.DISH_COLUMN_NAME, dbHelper.DISH_COLUMN_DESCRIPTION, dbHelper.DISH_COLUMN_RATING, dbHelper.DISH_COLUMN_BITMAPLOCATION};

    public CountriesDataSource(Context cntxt)
    {
        this.context = cntxt;
        dbHelper = new DatabaseHelper(cntxt);
    }

    public void open()
    {
        database = dbHelper.getWritableDatabase();
    }

    public void close()
    {
        dbHelper.close();
    }

    public void storeCountry(Country c)
    {
        ContentValues values = new ContentValues();
        values.put(dbHelper.COUNTRY_COLUMN_ALPHA3, c.getAlpha3Code());
        values.put(dbHelper.COUNTRY_COLUMN_ALPHA2, c.getAlpha2Code());
        values.put(dbHelper.COUNTRY_COLUMN_NAME, c.getName());
        values.put(dbHelper.COUNTRY_COLUMN_CAPITAL, c.getCapital());
        values.put(dbHelper.COUNTRY_COLUMN_POPULATION, c.getPopulation());
        values.put(dbHelper.COUNTRY_COLUMN_LAT, c.getLocation().latitude);
        values.put(dbHelper.COUNTRY_COLUMN_LONG, c.getLocation().longitude);
        values.put(dbHelper.COUNTRY_COLUMN_BITMAPLOCATION, c.getAlpha3Code().toLowerCase() + ".png");

        long insertId = database.insert(dbHelper.TABLE_COUNTRY,null,values);

        if(!String.valueOf("-1").equals(String.valueOf(insertId)))
        {
            Log.i("Info",c.getName() + " added to local database");
        }

        else
        {
            Log.e("Debug", "error adding country to database");
        }
    }

    public void storeDish(Dish d)
    {
        ContentValues values = new ContentValues();
        values.put(dbHelper.DISH_COLUMN_ALPHA3, d.getCountryAlpha3());
        values.put(dbHelper.DISH_COLUMN_NAME, d.getName());
        values.put(dbHelper.DISH_COLUMN_DESCRIPTION, d.getDescription());
        values.put(dbHelper.DISH_COLUMN_RATING, d.getRating());
        values.put(dbHelper.DISH_COLUMN_DATE, d.getDate());
        values.put(dbHelper.DISH_COLUMN_BITMAPLOCATION, "null");

        long insertId = database.insert(dbHelper.TABLE_DISH,null,values);

        if(!String.valueOf("-1").equals(String.valueOf(insertId)))
        {
            Log.i("Info", d.getName() + " added to local database for country " + d.getCountryAlpha3());
        }

        else
        {
            Log.e("Error", "error adding country to database");
        }
    }

    public List<Country> getAllCountries() {
        List<Country> countries = new ArrayList<Country>();

        Cursor cursor = database.query(dbHelper.TABLE_COUNTRY,
                allCountryColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Country country = cursorToCountry(cursor);

            Cursor dishescursor = database.query(dbHelper.TABLE_DISH,
                    allDishColumns, dbHelper.DISH_COLUMN_ALPHA3 + " = '" + country.getAlpha3Code() + "'", null, null, null, null);

            Log.i("Info", "Found " + dishescursor.getCount() + " dishes for " + country.getName());

            dishescursor.moveToFirst();

            while(!dishescursor.isAfterLast())
            {
                country.addDish(cursorToDish(dishescursor, country));

                dishescursor.moveToNext();
            }

            countries.add(country);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return countries;
    }

    private Country cursorToCountry(Cursor cursor) {
        Country country = new Country();


        country.setAlpha3Code(cursor.getString(0));
        country.setAlpha2Code(cursor.getString(1));
        country.setName(cursor.getString(2));
        country.setCapital(cursor.getString(3));
        country.setPopulation(cursor.getInt(4));
        country.setLocation(cursor.getDouble(5), cursor.getDouble(6));
        country.setCountryFlag(new ImageSaver(context).
                setFileName(cursor.getString(7)).
                setDirectoryName("images").
                load());

        return country;
    }

    private Dish cursorToDish(Cursor cursor, Country country) {
        Dish dish = new Dish();

        dish.setCountry(country);
        dish.setDate(new Date(100));
        dish.setName(cursor.getString(2));
        dish.setDescription(cursor.getString(3));
        dish.setRating(Float.valueOf(100));
        dish.setBitmapLocation("location");

        return dish;
    }
}
