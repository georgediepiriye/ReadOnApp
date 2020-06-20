package com.example.readon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private ProgressBar mProgressBar;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        String query = intent.getStringExtra("Query");
        URL bookUrl;

        mProgressBar = findViewById(R.id.progress_bar);
        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager booksLayoutManger = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(booksLayoutManger);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("ReadOn");
        toolbar.setTitleTextColor(Color.WHITE);

        try {
            if (query == null || query.isEmpty()) {
                bookUrl = ApiUtil.buildUrl("late");
            } else {
                bookUrl = new URL(query);
            }

            new BooksAsyncTask().execute(bookUrl);

        } catch (Exception e) {
            Log.d("Error", e.getMessage());
        }
    }


    //adds menu to the action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.book_list_menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setIconified(true);
        searchView.setQueryHint("Search...");
        ArrayList<String> recentList = SpUtil.getQueryLIst(getApplicationContext());
        int itemNum = recentList.size();
        MenuItem recentMenu;
        for(int i = 0 ; i<itemNum; i++){
            recentMenu = menu.add(Menu.NONE,i,Menu.NONE,recentList.get(i));
        }
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.advanced_search:
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                return true;

            default:
                int position = item.getItemId() + 1;
                String preferenceName = SpUtil.QUERY + String.valueOf(position);
                String query = SpUtil.getPreferenceString(getApplicationContext(),preferenceName);
                String[] prefParams = query.split("\\,");
                String[] queryParams = new String[4];
                for(int i = 0;i<prefParams.length;i++){
                    queryParams[i] = prefParams[i];
                }
                URL bookUrl = ApiUtil.buildSearchUrl(
                        (queryParams[0]==null)? "": queryParams[0],
                        (queryParams[1]==null)? "": queryParams[1],
                        (queryParams[2]==null)? "": queryParams[2],
                        (queryParams[3]==null)? "": queryParams[3]
                );
                new BooksAsyncTask().execute(bookUrl);


                return super.onOptionsItemSelected(item);
        }

    }

    //builds a new url with query entered in search widget and performs a search with it
    @Override
    public boolean onQueryTextSubmit(String query) {
        try {
            URL bookUrl = ApiUtil.buildUrl(query);
            new BooksAsyncTask().execute(bookUrl);
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
        }

        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    //performs all network calls on the background thread
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
                BookAdapter adapter = new BookAdapter(getApplicationContext(), books);
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