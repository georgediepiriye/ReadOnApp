package com.example.readon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private ProgressBar mProgressBar;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mProgressBar = findViewById(R.id.progress_bar);
        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager booksLayoutManger = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(booksLayoutManger);


        try {
            URL bookUrl = ApiUtil.buildUrl("cooking");
            new BooksAsyncTask().execute(bookUrl);

        } catch (Exception e) {
            Log.d("Error", e.getMessage());
        }
    }


    @SuppressLint("StaticFieldLeak")
    public class BooksAsyncTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            URL searchUrl = urls[0];
            String result = null;
            try {
                result = ApiUtil.getJson(searchUrl);
            } catch (IOException e) {
                Log.e("Error", e.getMessage());
            }

            return result;
        }

        //method called after network connection is made
        @Override
        protected void onPostExecute(String result) {
            TextView errorTextView = findViewById(R.id.error_text_view);
            mProgressBar.setVisibility(View.INVISIBLE);
            if (result == null) {
                recyclerView.setVisibility(View.INVISIBLE);
                errorTextView.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                errorTextView.setVisibility(View.INVISIBLE);

                ArrayList<Book> books = ApiUtil.getBooksFromJson(result);
                BookAdapter adapter = new BookAdapter(books);
                recyclerView.setAdapter(adapter);
            }


        }

        //method called before network connection is made
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }
}