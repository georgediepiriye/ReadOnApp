package com.example.readon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BooksViewHolder> {
    ArrayList<Book> books;
    public BookAdapter(ArrayList<Book> books){
        this.books = books;
    }

    @NonNull
    @Override
    public BooksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.recycler_list_item,parent,false);
        return new BooksViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BooksViewHolder holder, int position) {
        Book book =books.get(position);
        holder.bind(book);

    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public  class BooksViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle;
        TextView tvAuthors;
        TextView tvPublisher;
        TextView tvDate;

        public BooksViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle= itemView.findViewById(R.id.list_item_title);
            tvAuthors = itemView.findViewById(R.id.list_item_author);
            tvPublisher = itemView.findViewById(R.id.list_item_publisher);
            tvDate = itemView.findViewById(R.id.list_item_date);
        }

        public  void bind(Book book){
            tvTitle.setText(book.title);
            String authors ="";
            int i = 0;
            for (String author : book.authors){
                authors+=author;
                i++;
                if(i<book.authors.length){
                    authors+=", ";
                }

            }
            tvAuthors.setText(authors);
            tvPublisher.setText(book.publisher);
            tvDate.setText(book.publishedDate);


        }
    }
}
