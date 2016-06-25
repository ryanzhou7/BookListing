package com.ryanzhou.company.booklisting;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ryanzhou.company.booklisting.model.Book;

import java.util.List;

public class MyBookRecyclerViewAdapter extends RecyclerView.Adapter<MyBookRecyclerViewAdapter.ViewHolder> {

    public final String LOG_TAG = getClass().getSimpleName();
    private final List<Book> mValues;

    public MyBookRecyclerViewAdapter(List<Book> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = getmValues().get(position);
        ;
        holder.mTextViewAuthor.setText(holder.mItem.formatAuthorNamesForDisplay());
        holder.mTextViewTitle.setText(holder.mItem.getmTitle());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, holder.mItem.getmTitle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return getmValues().size();
    }

    public List<Book> getmValues() {
        return mValues;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTextViewTitle;
        public final TextView mTextViewAuthor;
        public Book mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTextViewAuthor = (TextView) view.findViewById(R.id.textViewAuthor);
            mTextViewTitle = (TextView) view.findViewById(R.id.textViewTitle);
        }
    }
}
