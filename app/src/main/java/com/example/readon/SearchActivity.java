package com.example.readon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.URL;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        Toolbar toolbar = findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Advanced Search");
        toolbar.setTitleTextColor(Color.WHITE);

        final EditText editTextTitle = findViewById(R.id.search_edit_text_title);
        final EditText editTextAuthor = findViewById(R.id.search_edit_text_authors);
        final EditText editTextPublisher = findViewById(R.id.search_edit_text_publisher);
        final EditText editTextIsbn = findViewById(R.id.search_edit_text_isbn);
        final Button button = findViewById(R.id.search_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = editTextTitle.getText().toString().trim();
                String author = editTextAuthor.getText().toString().trim();
                String publisher = editTextPublisher.getText().toString().trim();
                String isbn = editTextIsbn.getText().toString().trim();
                if (title.isEmpty() && author.isEmpty() && publisher.isEmpty() && isbn.isEmpty()) {
                    Toast.makeText(SearchActivity.this, "Please insert valid search terms", Toast.LENGTH_SHORT).show();
                } else {
                    URL queryUrl = ApiUtil.buildSearchUrl(title, author, publisher, isbn);
                    //Shared preferences
                    Context context = getApplicationContext();
                    int position = SpUtil.getPreferenceInt(context,SpUtil.POSITION);
                    if(position==0 || position == 5){
                        position = 1;
                    }else{
                        position ++;
                    }
                    String key = SpUtil.QUERY + position;
                    String value = title + "," + author + "," + publisher + "," + isbn;
                    SpUtil.setPreferenceString(context,key,value);
                    SpUtil.setPreferenceInt(context,SpUtil.POSITION ,position);

                    String query = queryUrl.toString();
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    intent.putExtra("Query", query);
                    startActivity(intent);
                }
            }
        });
    }
}