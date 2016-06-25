package com.ryanzhou.company.booklisting;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.ryanzhou.company.booklisting.model.Book;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ryanzhou on 6/24/16.
 */

//with code adoption from https://github.com/udacity/Sunshine-Version-2
public class FetchBooksTask extends AsyncTask<String, Void, String> {
    public final String LOG_TAG = getClass().getSimpleName();
    private final String METHOD = "GET";
    private final String BASE_URL = "https://www.googleapis.com";
    private final String FEATURE = "books";
    private final String VERSION = "v1";
    private final String DATA_TYPE = "volumes";
    private final String MAX_RESULTS_KEY = "maxResults";
    private final String NUM_RESULTS = "10";
    private final String ITEMS_ARRAY_KEY = "items";
    private final String TOTAL_ITEMS_KEY = "totalItems";
    private FetchBookTaskListener mListener;

    public FetchBooksTask(Context context) {
        if (context instanceof FetchBookTaskListener) {
            mListener = (FetchBookTaskListener) context;
        } else {
            Log.e(LOG_TAG, context.toString() + " must implement FetchBookTaskListener");
        }
    }

    @Override
    protected String doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String booksJsonStr = null;
        try {
            URL fullUrl = new URL(buildFullUrlWithQuery(params[0]));
            urlConnection = (HttpURLConnection) fullUrl.openConnection();
            urlConnection.setRequestMethod(METHOD);
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null)
                return null;
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null)
                buffer.append(line);
            if (buffer.length() == 0)
                return null;
            booksJsonStr = buffer.toString();
            return booksJsonStr;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            mListener.networkError();
            return null;
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
    }

    private String buildFullUrlWithQuery(String term) {
        StringBuilder stringBuilderUrl = new StringBuilder(BASE_URL);
        stringBuilderUrl.append("/" + FEATURE);
        stringBuilderUrl.append("/" + VERSION);
        stringBuilderUrl.append("/" + DATA_TYPE);
        stringBuilderUrl.append("?q=" + term);
        stringBuilderUrl.append("&" + MAX_RESULTS_KEY + "=" + NUM_RESULTS);
        return stringBuilderUrl.toString();
    }

    @Override
    protected void onPostExecute(String result) {
        if (result == null) {
            return;
        }
        try {
            JSONObject rootResult = new JSONObject(result);
            String totalItems = rootResult.getString(TOTAL_ITEMS_KEY);
            if (Integer.valueOf(totalItems) == 0) {
                mListener.noResultsFound();
                return;
            } else {
                JSONArray booksResult = rootResult.getJSONArray(ITEMS_ARRAY_KEY);
                createBookModelFromJsonArray(booksResult);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void createBookModelFromJsonArray(JSONArray booksResult) {
        if (booksResult == null || booksResult.length() == 0) {
            mListener.fetchBooksDone(null);
        }
        List<Book> books = new ArrayList<>();
        for (int i = 0; i < booksResult.length(); i++) {
            try {
                JSONObject jsonObject = booksResult.getJSONObject(i);
                JSONObject jsonObjectVolume = jsonObject.getJSONObject("volumeInfo");
                JSONArray authors = jsonObjectVolume.getJSONArray("authors");
                String[] authorsArray = new String[authors.length()];
                for (int j = 0; j < authors.length(); j++)
                    authorsArray[j] = authors.getString(j);
                String title = jsonObjectVolume.getString("title");
                books.add(new Book(authorsArray, title));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        mListener.fetchBooksDone(books);
    }

    public interface FetchBookTaskListener {
        void fetchBooksDone(List<Book> books);

        void noResultsFound();

        void networkError();
    }

}