package br.edu.ifsuldeminas.mch.cuidemaisapp.external;

import android.util.JsonReader;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.xml.sax.XMLReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import br.edu.ifsuldeminas.mch.cuidemaisapp.MainActivity;

public class CuideMaisAPI {
    //private static final String diaperHumidityKey = "field2";
    private static final String environmentTemperatureKey = "field1";
    //private static final String environmentHumidityKey = "field3";
    private static final String urlString = "https://api.thingspeak.com/channels/2178271/fields/1.json?api_key=XJ52TCXFWXWEFVZ5&results=1";


    //private double diaperHumidity = Double.NEGATIVE_INFINITY;
    //private double environmentHumidity = Double.NEGATIVE_INFINITY;
    private double environmentTemperature = Double.NEGATIVE_INFINITY;

    public CuideMaisAPI() {
        try {
            JSONObject jsonObject = getJSONObjectFromURL();
            JSONArray feeds = jsonObject.getJSONArray("feeds");
            if (feeds.length() > 0) {
                JSONObject latestEntry = feeds.getJSONObject(feeds.length() - 1);
                environmentTemperature = Double.parseDouble(latestEntry.getString(environmentTemperatureKey).replace("r", ""));
            }

            //Log.e("br.edu.ifsuldeminas.mch.cuidemaisapp.external", jsonObject.toString());

            //diaperHumidity = Double.parseDouble(json.get(diaperHumidityKey).toString());
            //environmentHumidity = Double.parseDouble(json.get(environmentHumidityKey).toString());
            environmentTemperature = Double.parseDouble(jsonObject.get(environmentTemperatureKey).toString());
        } catch (Exception e) {
            Log.e("br.edu.ifsuldeminas.mch.cuidemaisapp.external.CuideMaisAPI", e.getMessage(), e);
        }
    }

    //public double getDiaperHumidity() {
        //return diaperHumidity;
    //}

    //public int getDiaperHumidityPercent() {
        //if (diaperHumidity != Double.NEGATIVE_INFINITY)
            //return calculateDiaperHumidityPercent(diaperHumidity);

        //return 0;
    //}

    //public static int calculateDiaperHumidityPercent(double value){
        //return (int) ((1024 - value) / 4.24);
    //}

    //public double getEnvironmentHumidity() {
        //return environmentHumidity;
    //}

    public double getEnvironmentTemperature() {
        return environmentTemperature;
    }

    private static JSONObject getJSONObjectFromURL() throws IOException, JSONException {
        URL url = new URL(urlString);
        HttpURLConnection urlConnection;
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setReadTimeout(10000); /* milisgundos */
        urlConnection.setConnectTimeout(15000); /* milisgundos */
        urlConnection.setDoOutput(true);
        urlConnection.connect();

        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line+"\n");
        }

        br.close();
        urlConnection.disconnect();

        return new JSONObject(sb.toString());
    }
}