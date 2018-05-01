package com.example.anna.apptextreader;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetQuote extends AsyncTask<Void, Void, Integer>{
    static String quote = "";
    @Override
    protected Integer doInBackground(Void... voids) {

            final String SERVICE_URL = "http://quotes.rest/qod.json";
            Gson gson = new Gson();
            try {
                URL url = new URL(SERVICE_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                String response = "";
                InputStream inputStream = connection.getInputStream();
                int data = inputStream.read();
                while (data != -1) {
                    response += (char) data;
                    data = inputStream.read();
                }
                inputStream.close();
                quote = response;
                Log.d("fuck", response);
            } catch (Exception e) {
                Log.d("fuck", e.toString());
            }
            return 0;

    }
}
