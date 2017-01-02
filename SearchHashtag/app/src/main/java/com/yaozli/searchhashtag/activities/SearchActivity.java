package com.yaozli.searchhashtag.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yaozli.searchhashtag.R;
import com.yaozli.searchhashtag.adapter.SavedTweetAdapter;
import com.yaozli.searchhashtag.adapter.TweetAdapter;
import com.yaozli.searchhashtag.dto.TweetDTO;
import com.yaozli.searchhashtag.util.AdminSQLiteOpenHelper;
import com.yaozli.searchhashtag.util.ImageUtil;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private Button btnSearch;
    private EditText txtSearch;
    private AdminSQLiteOpenHelper admin;
    private SQLiteDatabase bd;
    private ArrayList<TweetDTO> tweets;
    private RecyclerView recyclerView;
    private SavedTweetAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        admin = new AdminSQLiteOpenHelper(this, "twitter", null, 1);
        bd = admin.getWritableDatabase();
        recyclerView = (RecyclerView) findViewById(R.id.saveHashtag);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        txtSearch = (EditText) findViewById(R.id.txtSearch);
        txtSearch.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    Intent intent = new Intent(SearchActivity.this, TweetsActivity.class);
                    intent.putExtra("ht", txtSearch.getText().toString());
                    startActivity(intent);

                    return true;
                }
                return false;
            }
        });



        getTweets();


    }

    public void getTweets(){
        GetTweets async = new GetTweets();
        async.execute();
    }


    private class GetTweets extends AsyncTask<Void, Integer, Void> {
        Cursor c;

        @Override
        protected void onPreExecute(){
             c = bd.rawQuery(
                    "select id, stringImg, username, content from tweets", null);
        }


        @Override
        protected Void doInBackground(Void... params) {


            while (c.moveToNext()) {
                TweetDTO t = new TweetDTO();
                t.setId(c.getString(0));
                t.setImage(ImageUtil.getBitmapFromUrl(c.getString(1)));
                t.setUsername(c.getString(2));
                t.setContent(c.getString(3));
                tweets.add(t);
                Log.e("img",c.getString(1));
            }
            return null;
        }


        @Override
        protected void onCancelled() {
            super.onCancelled();

        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            Log.e("Progresando", "Progresando");
        }


        @Override
        protected void onPostExecute(Void result) {
            Log.e("Terminando", "Ando");
            adapter = new SavedTweetAdapter(tweets, SearchActivity.this);
            recyclerView.setAdapter(adapter);


        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        tweets = new ArrayList<>();
        getTweets();
    }
}



