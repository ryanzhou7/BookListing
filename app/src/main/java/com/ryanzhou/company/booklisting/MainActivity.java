package com.ryanzhou.company.booklisting;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.ryanzhou.company.booklisting.model.Book;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FetchBooksTask.FetchBookTaskListener {

    public final String LOG_TAG = getClass().getSimpleName();
    private MyBookRecyclerViewAdapter myBookRecyclerViewAdapter;
    private TextView textViewInfoMessage;
    private RecyclerView recyclerView;
    private EditText editTextQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextQuery = (EditText) findViewById(R.id.editTextQuery);
        textViewInfoMessage = (TextView) findViewById(R.id.textViewError);
        recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        myBookRecyclerViewAdapter = new MyBookRecyclerViewAdapter(new ArrayList<Book>());
        recyclerView.setAdapter(myBookRecyclerViewAdapter);
        if (Utility.isNetworkAvailable(getApplicationContext()))
            textViewInfoMessage.setVisibility(View.GONE);

        editTextQuery.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE)) {
                    if (Utility.isNetworkAvailable(getApplicationContext())) {
                        FetchBooksTask fetchBooksTask = new FetchBooksTask(MainActivity.this);
                        fetchBooksTask.execute(editTextQuery.getText().toString());
                    } else
                        displayNetworkError();

                }
                return false;
            }
        });

    }

    @Override
    public void fetchBooksDone(List<Book> books) {
        clearListItems();
        if (books == null)
            displayNetworkError();
        else {
            textViewInfoMessage.setVisibility(View.GONE);
            List<Book> list = myBookRecyclerViewAdapter.getmValues();
            list.addAll(books);
        }
    }

    @Override
    public void noResultsFound() {
        textViewInfoMessage.setVisibility(View.VISIBLE);
        textViewInfoMessage.setText(getString(R.string.no_results_from_query_message));
    }

    @Override
    public void networkError() {
        displayNetworkError();
    }

    private void displayNetworkError() {
        textViewInfoMessage.setVisibility(View.VISIBLE);
        textViewInfoMessage.setText(getString(R.string.no_network_error_message));
    }

    private void clearListItems() {
        List<Book> list = myBookRecyclerViewAdapter.getmValues();
        list.clear();
        myBookRecyclerViewAdapter.notifyDataSetChanged();
    }

}
