package com.sam_chordas.android.stockhawk.service;

import android.os.AsyncTask;
import android.util.Log;

import com.sam_chordas.android.stockhawk.rest.QuoteCursorAdapter;
import com.sam_chordas.android.stockhawk.rest.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by kshitijsharma on 02/04/16.
 */
public class StockDetailsTaskService extends AsyncTask<Void, Void, ArrayList> {

    private static final String TAG = StockDetailsTaskService.class.getCanonicalName();
    private QuoteCursorAdapter mCursorAdapter;
    String getResponse;
    private ArrayList<String> arrayList;
    public static ArrayList<String> xAxis;

    public StockDetailsTaskService(QuoteCursorAdapter mCursorAdapter) {
        this.mCursorAdapter = mCursorAdapter;
        arrayList = new ArrayList<>();
        xAxis = new ArrayList<>();
    }

    @Override
    protected ArrayList doInBackground(Void... params) {

        StringBuilder urlStringBuilder = new StringBuilder();
        urlStringBuilder.append("https://query.yahooapis.com/v1/public/yql?q=");
        try {
            urlStringBuilder.append(URLEncoder.encode("select * from yahoo.finance.historicaldata " +
                            "where symbol = \"" + mCursorAdapter.getCursor().getString(1).toString() + "\" " +
                            "and endDate = \"" + Utils.currentDate() + "\" and startDate = \"" + Utils.currentDateOneYearAgo() + "\" "
                    , "UTF-8"));

            urlStringBuilder.append("&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables."
                    + "org%2Falltableswithkeys&callback=");

            String urlString;

            if (urlStringBuilder != null) {
                urlString = urlStringBuilder.toString();
                try {
                    getResponse = StockTaskService.fetchData(urlString);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        parseYAxisValues(getResponse);

        return arrayList;
    }

    private void parseYAxisValues(String getResponse) {

        try {
            JSONObject jsonObject = new JSONObject(getResponse);
            JSONObject jsonObjectResults = jsonObject.getJSONObject("query").getJSONObject("results");
            JSONArray quotes = jsonObjectResults.getJSONArray("quote");

            for (int i = 0; i < quotes.length(); i++) {
                JSONObject results = quotes.getJSONObject(i);
                arrayList.add(results.getString("Open"));
                xAxis.add((results.getString("Date")));
            }

            Log.d(TAG, "parseYAxisValues() called with: " + "getResponse = [" + getResponse + "]");

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
