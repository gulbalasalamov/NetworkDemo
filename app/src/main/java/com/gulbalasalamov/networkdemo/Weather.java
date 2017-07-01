package com.gulbalasalamov.networkdemo;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Gulbala on 01.07.2017.
 */

public class Weather extends AsyncTask<String, Void, String> {
    String result = "";

    @Override
    protected String doInBackground(String... urls) {

        URL url;
        HttpURLConnection urlConnection = null;

        try {
            url = new URL(urls[0]);

            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = urlConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);

            int data = reader.read();

            while (data != -1) {
                char current = (char) data;
                result += current;
                data = reader.read();
            }
            return result; //JSON

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONObject weatherData = new JSONObject(jsonObject.getString("main"));

            double temperature = Double.parseDouble(weatherData.getString("temp"));
            int temperatureInteger = (int) (temperature - 273.15);

            String placeName = jsonObject.getString("name");
            MainActivity.tv_temperature.setText(String.valueOf(temperatureInteger));
            MainActivity.tv_place.setText(placeName);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
