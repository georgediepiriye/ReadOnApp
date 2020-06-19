package com.example.readon;

import android.net.Uri;
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
    public static final String QUERY_PARAMETER_KEY = "q";
    public static final String KEY = "key";
    public static final String API_KEY = "AIzaSyC3mmRYm2FSFcoPSN5z42Uc4DnOPuk-tAk";
    public static final String TITLE = "intitle:";
    public static final String AUTHOR = "inauthor:";
    public static final String PUBLISHER = "inpublisher:";
    public static final String ISBN = "inisbn:";

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

    //method to build advanced search url
    public static URL buildSearchUrl(String title, String author, String publisher, String isbn) {
        URL url = null;
        StringBuilder stringBuilder = new StringBuilder();
        if (!title.isEmpty()) stringBuilder.append(TITLE).append(title).append("+");
        if (!author.isEmpty()) stringBuilder.append(AUTHOR).append(author).append("+");
        if (!publisher.isEmpty()) stringBuilder.append(PUBLISHER).append(publisher).append("+");
        if (!isbn.isEmpty()) stringBuilder.append(ISBN).append(isbn).append("+");
        stringBuilder.setLength(stringBuilder.length() - 1);
        String query = stringBuilder.toString();
        Uri uri = Uri.parse(BASE_API_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAMETER_KEY, query)
                .appendQueryParameter(KEY, API_KEY)
                .build();
        try {
            url = new URL(uri.toString());
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
        final String DESCRIPTION = "description";
        final String IMAGE_LINKS = "imageLinks";
        final String THUMBNAIL = "thumbnail";

        ArrayList<Book> books = new ArrayList<>();
        try {
            JSONObject jsonBooks = new JSONObject(json);
            JSONArray arrayBooks = jsonBooks.getJSONArray(ITEMS);
            int numberOfBooks = arrayBooks.length();
            for (int i = 0; i < numberOfBooks; i++) {
                JSONObject bookJson = arrayBooks.getJSONObject(i);
                JSONObject volumeInfoJson = bookJson.getJSONObject(VOLUME_INFO);
                JSONObject imageLinksJson;
                imageLinksJson = volumeInfoJson.getJSONObject(IMAGE_LINKS);
                int authorNum;
                try {
                    authorNum = volumeInfoJson.getJSONArray(AUTHORS).length();
                } catch (JSONException e) {
                    authorNum = 0;
                }

                String[] authors = new String[authorNum];
                for (int j = 0; j < authorNum; j++) {
                    authors[j] = volumeInfoJson.getJSONArray(AUTHORS).get(j).toString();
                }
                Book book = new Book(bookJson.getString(ID), volumeInfoJson.getString(TITLE),
                        (volumeInfoJson.isNull(SUB_TITLE) ? "" : volumeInfoJson.getString(SUB_TITLE)),
                        authors, volumeInfoJson.isNull(PUBLISHER) ? "" : volumeInfoJson.getString(PUBLISHER), volumeInfoJson.isNull(PUBLISHED_DATE) ? "" : volumeInfoJson.getString(PUBLISHED_DATE),
                        volumeInfoJson.isNull(DESCRIPTION) ? "" : volumeInfoJson.getString(DESCRIPTION), imageLinksJson.isNull(THUMBNAIL) ? "" :
                        imageLinksJson.getString(THUMBNAIL));
                books.add(book);


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return books;
    }

}
