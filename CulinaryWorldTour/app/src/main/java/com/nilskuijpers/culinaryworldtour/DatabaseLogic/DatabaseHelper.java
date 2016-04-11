package com.nilskuijpers.culinaryworldtour.DatabaseLogic;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by surfa on 30-3-2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TABLE_COUNTRY = "country";
    public static final String COUNTRY_COLUMN_ALPHA2 = "country_alpha2code";
    public static final String COUNTRY_COLUMN_ALPHA3 = "country_alpha3code";
    public static final String COUNTRY_COLUMN_NAME = "country_name";
    public static final String COUNTRY_COLUMN_CAPITAL = "country_capital";
    public static final String COUNTRY_COLUMN_POPULATION = "country_population";
    public static final String COUNTRY_COLUMN_LAT = "country_lat";
    public static final String COUNTRY_COLUMN_LONG = "country_long";
    public static final String COUNTRY_COLUMN_BITMAPLOCATION = "country_bitmaplocation";

    public static final String TABLE_DISH = "dish";
    public static final String DISH_COLUMN_ID = "id";
    public static final String DISH_COLUMN_ALPHA3 = "dish_country_alpha3";
    public static final String DISH_COLUMN_DATE = "dish_country_date";
    public static final String DISH_COLUMN_NAME = "dish_name";
    public static final String DISH_COLUMN_DESCRIPTION = "dish_description";
    public static final String DISH_COLUMN_RATING = "dish_rating";
    public static final String DISH_COLUMN_BITMAPLOCATION = "dish_bitmaplocation";

    private static final String DATABASE_NAME = "CulinaryWorldTour.db";
    private static final int DATABASE_VERSION = 8;

    private static final String COUNTRY_CREATE = "create table "
            + TABLE_COUNTRY + "(" + COUNTRY_COLUMN_ALPHA3 + " text primary key, "
            + COUNTRY_COLUMN_ALPHA2 + " text not null, "
            + COUNTRY_COLUMN_NAME + " text not null, "
            + COUNTRY_COLUMN_CAPITAL + " text not null, "
            + COUNTRY_COLUMN_POPULATION + " integer not null, "
            + COUNTRY_COLUMN_LAT + " float not null, "
            + COUNTRY_COLUMN_LONG + " float not null, "
            + COUNTRY_COLUMN_BITMAPLOCATION + " text);";

    private static final String DISH_CREATE = "create table "
            + TABLE_DISH + "(" + DISH_COLUMN_ID
            + " integer primary key autoincrement, "
            + DISH_COLUMN_ALPHA3 + " text not null, "
            + DISH_COLUMN_DATE + " text not null, "
            + DISH_COLUMN_NAME + " text not null, "
            + DISH_COLUMN_DESCRIPTION + " text not null, "
            + DISH_COLUMN_RATING + " float not null, "
            + DISH_COLUMN_BITMAPLOCATION + " text, "
            + " FOREIGN KEY (" + DISH_COLUMN_ALPHA3 + ") REFERENCES " + TABLE_COUNTRY + "(" + COUNTRY_COLUMN_ALPHA3 + "));";

    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(COUNTRY_CREATE);
        db.execSQL(DISH_CREATE);
        Log.d("SQL", COUNTRY_CREATE);
        Log.d("SQL", DISH_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COUNTRY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DISH);
        onCreate(db);

    }
}
