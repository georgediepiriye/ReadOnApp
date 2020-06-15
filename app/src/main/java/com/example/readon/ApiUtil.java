package com.example.readon;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class ApiUtil {
    public static final String BASE_API_URL = "https://www.googleapis.com/books/v1/volumes";

    private ApiUtil() {
    }

    //builds the complete url
    public static URL buildUrl(String title) {
        String fullUrl = BASE_API_URL + "?q=" + title;
        URL url = null;
        try {
            url = new URL(fullUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;

    }

    /**
     * gets the built url,makes internet connection and returns a json result
     */
    public static String getJson(URL url) throws IOException {

        //connects the url to the internet
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            //gets the result as a stream
            InputStream stream = connection.getInputStream();
            //converts the stream to a string
            Scanner scanner = new Scanner(stream);
            scanner.useDelimiter("//A");
            boolean hasData = scanner.hasNext();
            if (hasData) {
                return scanner.next();
            } else {
                return null;
            }
        } catch (Exception e) {
            Log.e("Error", e.toString());
            return null;
        } finally {
            connection.disconnect();

        }

    }


    //method to get values from the json
    public static ArrayList<Book> getBooksFromJson(String json) {
        final String ID = "id";
        final String TITLE = "title";
        final String SUB_TITLE = "subtitle";
        final String AUTHORS = "authors";
        final String PUBLISHER = "publisher";
        final String PUBLISHED_DATE = "publishedDate";
        final String ITEMS = "items";
        final String VOLUME_INFO = "volumeInfo";

        ArrayList<Book> books = new ArrayList<>();
        try {
            JSONObject jsonBooks = new JSONObject(json);
            JSONArray arrayBooks = jsonBooks.getJSONArray(ITEMS);
            int numberOfBooks = arrayBooks.length();
            for (int i = 0; i < numberOfBooks; i++) {
                JSONObject bookJson = arrayBooks.getJSONObject(i);
                JSONObject volumeInfoJson = bookJson.getJSONObject(VOLUME_INFO);
                int authorNum = volumeInfoJson.getJSONArray(AUTHORS).length();
                String[] authors = new String[authorNum];
                for (int j = 0; j < authorNum; j++) {
                    authors[j] = volumeInfoJson.getJSONArray(AUTHORS).get(j).toString();
                }
                Book book = new Book(bookJson.getString(ID), volumeInfoJson.getString(TITLE),
                        (volumeInfoJson.isNull(SUB_TITLE) ? "" : volumeInfoJson.getString(SUB_TITLE)),
                        authors, volumeInfoJson.getString(PUBLISHER), volumeInfoJson.getString(PUBLISHED_DATE));
                books.add(book);


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return books;
    }

}
