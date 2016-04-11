package com.nilskuijpers.culinaryworldtour.NetworkLogic;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.nilskuijpers.culinaryworldtour.Country;
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

/**
 * Created by surfa on 29-3-2016.
 */
public class CountryParser extends AsyncTask<String, Void, Country>
{
    private CountriesDataSource cds;
    private ProgressDialog progressDialog;
    private Context context;
    private Country tempCountry;
    private HttpURLConnection connection;
    private URL baseURL;
    private URL imageURL;
    private BufferedReader br;
    private InputStream is;
    private JSONObject countryJSONObject;
    private JSONArray LatitudeLongitudeJSONArray;
    private TaskDelegate delegate;

    private SharedObjects sharedObjects;

    public CountryParser(Context cntxt){
        this.context = cntxt;
        this.delegate = (MainActivity) cntxt;
    }

    @Override
    protected void onPreExecute()
    {
        this.cds = new CountriesDataSource(this.context);
        this.cds.open();
        this.sharedObjects = SharedObjects.getInstance();
        this.progressDialog = new ProgressDialog(this.context);
        this.progressDialog.setMessage("Loading country data...");
        this.progressDialog.setCancelable(false);
        //this.progressDialog.show();
    }


    @Override
    protected Country doInBackground(String... params) {
        try {
            this.baseURL = new URL(String.valueOf("https://restcountries.eu/rest/v1/alpha/" + params[0]));
            this.connection = (HttpURLConnection) this.baseURL.openConnection();
            this.connection.setRequestMethod("GET");
            this.connection.connect();


            int status = connection.getResponseCode();

            switch (status)
            {
                default:
                {
                    throw new Exception("HTTP Error " + String.valueOf(status));
                }
                case 200:
                {
                    this.br = new BufferedReader(new InputStreamReader(this.connection.getInputStream()));

                    String line = br.readLine();

                    this.br.close();

                    if(line != null)
                    {
                        this.countryJSONObject = new JSONObject(line);

                        this.LatitudeLongitudeJSONArray = this.countryJSONObject.getJSONArray("latlng");

                        Log.i("alpha3", this.countryJSONObject.getString("alpha3Code"));

                        this.tempCountry = new Country(this.countryJSONObject.getString("alpha2Code"), this.countryJSONObject.getString("alpha3Code"), this.countryJSONObject.getString("name"),this.countryJSONObject.getString("capital"),this.countryJSONObject.getInt("population"),new LatLng(this.LatitudeLongitudeJSONArray.getDouble(0),this.LatitudeLongitudeJSONArray.getDouble(1)));

                        Log.d("Line", line);

                        this.imageURL = new URL("http://www.geonames.org/flags/x/" + tempCountry.getAlpha2Code().toLowerCase() + ".gif");
                        this.connection = (HttpURLConnection) imageURL.openConnection();
                        this.connection.setDoInput(true);
                        this.connection.connect();

                        this.is = connection.getInputStream();

                        Bitmap tempImg = BitmapFactory.decodeStream(this.is);

                        this.tempCountry.setCountryFlag(tempImg);

                        new ImageSaver(context).
                                setFileName(this.tempCountry.getAlpha3Code().toLowerCase() + ".png").
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



