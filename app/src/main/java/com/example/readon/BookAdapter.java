package com.example.readon;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BooksViewHolder> {
    ArrayList<Book> books;
    Context context;

    public BookAdapter(Context context, ArrayList<Book> books) {
        this.context = context;
        this.books = books;
    }


    @NonNull
    @Override
    public BooksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.recycler_list_item, parent, false);
        return new BooksViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BooksViewHolder holder, int position) {
        Book book = books.get(position);

        String imageUri = book.thumbnail;
        ImageView image = holder.bookImage;
        Picasso.with(context).load(imageUri).placeholder(R.drawable.ic_baseline_menu_book_24).into(image);


        holder.bind(book);

    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public class BooksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvTitle;
        TextView tvAuthors;
        TextView tvPublisher;
        TextView tvDate;
        ImageView bookImage;

        public BooksViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.list_item_title);
            tvAuthors = itemView.findViewById(R.id.list_item_author);
            tvPublisher = itemView.findViewById(R.id.list_item_publisher);
            tvDate = itemView.findViewById(R.id.list_item_date);
            bookImage = itemView.findViewById(R.id.list_item_image);
            itemView.setOnClickListener(this);
        }

        public void bind(Book book) {
            tvTitle.setText(book.title);
            tvAuthors.setText(book.authors);
            tvPublisher.setText(book.publisher);
            tvDate.setText(book.publishedDate);


        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Book selectedBook = books.get(position);
            Intent intent = new Intent(view.getContext(), BookDetailActivity.class);
            intent.putExtra("Book", selectedBook);
            view.getContext().startActivity(intent);


        }
    }
}
