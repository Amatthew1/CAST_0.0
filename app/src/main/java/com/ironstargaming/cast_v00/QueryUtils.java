package com.ironstargaming.cast_v00;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Admin on 11/16/2016.
 */

public final class QueryUtils {
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();


    private QueryUtils() {
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the cell JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {

                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


    public static CellContainer fetchCellData(String requestUrl) {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }


        CellContainer cellContainer = extractCellFromJson(jsonResponse);
        /////////////////////////////////////////////fix return of extract


///////////////////////////// fix whatever eats fetch

        return cellContainer;
    }




/*
    public static List<Cell> fetchCellData(String requestUrl) {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }


        List<Cell> cells = extractCellFromJson(jsonResponse);


        return cells;
    }
*/

    private static CellContainer extractCellFromJson(String jsonResponse) {

        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }

        //////////////////////////////////////////////
        //get ready for some janky stuff
        //
        //////////////////////////////////////////////
        String string1 = "Name didnt stick";
        String string2 = "Faction didnt stick";
        boolean bool1 = true;
        boolean bool2 =true;


        List<Cell> childCells = new ArrayList<>();
        Cell centerCell=new Cell(string1,string2, bool1,bool2);
        Cell childCell;
        CellContainer cellContainer=new CellContainer(centerCell, childCells);


        try {


            JSONObject jsonCellContainer = new JSONObject(jsonResponse);

            String centerCellName = jsonCellContainer.getString("name");
            JSONObject properties = jsonCellContainer.getJSONObject("properties");
            String centerCellFaction = properties.getString("faction");
            Boolean centerCellPA = properties.getBoolean("player_awareness");
            Boolean centerCellLock = properties.getBoolean("locked");
            centerCell = new Cell(centerCellName, centerCellFaction, centerCellLock, centerCellPA);

            JSONArray childCellsArray = jsonCellContainer.getJSONArray("inner_cells");
            for (int i = 0; i < childCellsArray.length(); i++) {

                /////////////////this is broken AF
                String currentChildCell = childCellsArray.getString(i);
                //so bad. fix it!
                String childCellName = currentChildCell;
                ////////////////////////////////
                childCell = new Cell(childCellName, centerCellFaction, centerCellLock, centerCellPA);

                childCells.add(childCell);

            }

            cellContainer = new CellContainer(centerCell, childCells);


        } catch (JSONException e) {

            Log.e("QueryUtils", "Problem parsing the cell JSON results", e);
        }


        return cellContainer;
    }
    /*
    private static List<Cell> extractCellFromJson(String jsonResponse) {

        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }

        List<Cell> cells = new ArrayList<>();


        try {


            JSONObject Cell = new JSONObject(jsonResponse);

            String currentCellName = Cell.getString("name");
            JSONObject properties = Cell.getJSONObject("properties");
            String currentCellFaction = properties.getString("faction");
            Boolean currentCellPA = properties.getBoolean("player_awareness");
            Boolean currentCellLock = properties.getBoolean("locked");


            Cell cell = new Cell(currentCellName, currentCellFaction, currentCellLock, currentCellPA);
            cells.add(cell);


        } catch (JSONException e) {

            Log.e("QueryUtils", "Problem parsing the cell JSON results", e);
        }


        return cells;
    }
*/
}


