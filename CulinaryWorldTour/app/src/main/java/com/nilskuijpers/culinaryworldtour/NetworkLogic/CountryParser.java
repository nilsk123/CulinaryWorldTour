package com.nilskuijpers.culinaryworldtour.NetworkLogic;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.nilskuijpers.culinaryworldtour.Objects.Country;
import com.nilskuijpers.culinaryworldtour.DatabaseLogic.CountriesDataSource;
import com.nilskuijpers.culinaryworldtour.FilesystemLogic.ImageSaver;
import com.nilskuijpers.culinaryworldtour.MainActivity;
import com.nilskuijpers.culinaryworldtour.SharedObjects;
import com.nilskuijpers.culinaryworldtour.Interfaces.TaskDelegate;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CountryParser extends AsyncTask<String, Void, Country>
{
    private CountriesDataSource cds;
    private Context context;
    private TaskDelegate delegate;

    private SharedObjects sharedObjects;

    public CountryParser(Context context, TaskDelegate td){
        this.context = context;
        this.delegate = td;
    }

    @Override
    protected void onPreExecute()
    {
        this.cds = new CountriesDataSource(this.context);
        this.cds.open();
        this.sharedObjects = SharedObjects.getInstance();
    }


    @Override
    protected Country doInBackground(String... params) {
        try {
            URL baseURL = new URL(String.valueOf("https://restcountries.eu/rest/v1/alpha/" + params[0]));
            HttpURLConnection connection = (HttpURLConnection) baseURL.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();


            int status = connection.getResponseCode();

            switch (status)
            {
                default:
                {
                    throw new Exception("HTTP Error " + String.valueOf(status));
                }
                case 200:
                {
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    String line = br.readLine();

                    br.close();

                    if(line != null)
                    {
                        JSONObject countryJSONObject = new JSONObject(line);

                        JSONArray latitudeLongitudeJSONArray = countryJSONObject.getJSONArray("latlng");

                        Log.i("alpha3", countryJSONObject.getString("alpha3Code"));

                        Country tempCountry = new Country(countryJSONObject.getString("alpha2Code"), countryJSONObject.getString("alpha3Code"), countryJSONObject.getString("name"), countryJSONObject.getString("capital"), countryJSONObject.getInt("population"), new LatLng(latitudeLongitudeJSONArray.getDouble(0), latitudeLongitudeJSONArray.getDouble(1)));

                        Log.d("Line", line);

                        URL imageURL = new URL("http://www.geonames.org/flags/x/" + tempCountry.getAlpha2Code().toLowerCase() + ".gif");
                        connection = (HttpURLConnection) imageURL.openConnection();
                        connection.setDoInput(true);
                        connection.connect();

                        InputStream is = connection.getInputStream();

                        Bitmap tempImg = BitmapFactory.decodeStream(is);

                        tempCountry.setCountryFlag(tempImg);

                        new ImageSaver(context).
                                setFileName(tempCountry.getAlpha3Code().toLowerCase() + ".png").
                                setDirectoryName("images").
                                save(tempImg);

                        return tempCountry;
                    }
                    else
                    {
                        throw new Exception("Parser Error: No result found");
                    }
                }
            }
        }
        catch(Exception ex)
        {
            Log.e("Error",ex.getMessage() + params[0],ex);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Country c) {
        this.cds.storeCountry(c);
        this.cds.close();
        this.sharedObjects.setChosenCountry(c);
        delegate.TaskCompletionResult(c);
        //this.progressDialog.hide();
    }

}



