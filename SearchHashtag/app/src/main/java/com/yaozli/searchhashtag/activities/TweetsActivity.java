package com.yaozli.searchhashtag.activities;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import com.yaozli.searchhashtag.R;
import com.yaozli.searchhashtag.adapter.TweetAdapter;
import com.yaozli.searchhashtag.dto.TweetDTO;
import com.yaozli.searchhashtag.util.AdminSQLiteOpenHelper;
import com.yaozli.searchhashtag.util.TwitterUtil;

import java.util.ArrayList;

public class TweetsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TweetAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<TweetDTO> tweets;
    private String pHT;
    private Paint p = new Paint();
    private View view;
    private AdminSQLiteOpenHelper admin;
    private SQLiteDatabase bd;
    private ProgressDialog progress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweets);
        Bundle extras = getIntent().getExtras();
        pHT= extras.getString("ht");
        admin = new AdminSQLiteOpenHelper(this, "twitter", null, 1);
        bd = admin.getWritableDatabase();


        tweets = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.searchHashtag);

        recyclerView.setHasFixedSize(true);


        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        progress = new ProgressDialog(this);
        progress.setTitle("Cargando Tweets");
        progress.setMessage("Espere...");
        progress.setCancelable(false);
        GetHashtagAsynTask async = new GetHashtagAsynTask();
        async.execute();
        swipe();



    }
    private class GetHashtagAsynTask extends AsyncTask<Void, Integer, Void> {
        TwitterUtil tu;

        @Override
        protected void onPreExecute() {
            tu = new TwitterUtil();
            progress.show();
        }


        @Override
        protected Void doInBackground(Void... params) {

            tweets = tu.getTweets(pHT);
            return null;
        }


        @Override
        protected void onCancelled() {
            progress.dismiss();
            super.onCancelled();

        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            Log.e("Progresando", "Progresando");
        }


        @Override
        protected void onPostExecute(Void result) {
            progress.dismiss();
            if(tweets.size() == 0) {
                TweetDTO t = new TweetDTO();
                t.setUsername("@OSMI_INGENIA");
                t.setContent("NO HAY TWEETS QUE MOSTRAR CON ESE RESULTADO");
                Drawable myDrawable = getResources().getDrawable(R.drawable.dead);
                t.setImage(((BitmapDrawable) myDrawable).getBitmap());
                t.setImageString("http://www.roastbrief.com.mx/wp-content/uploads/2012/09/twitter-2.jpg");
                tweets.add(t);
            }

            adapter = new TweetAdapter(tweets, TweetsActivity.this);
            recyclerView.setAdapter(adapter);

        }
    }




    private void swipe(){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.RIGHT){
                    adapter.removeItem(position);

                } else if (direction == ItemTouchHelper.LEFT){
                    TweetDTO t = adapter.getItem(position);
                    Log.d("-","Iniciando");
                    ContentValues reg = new ContentValues();
                    reg.put("stringImg", t.getImageString());
                    reg.put("username",t.getUsername());
                    reg.put("content",t.getContent());
                    bd.insert("tweets", null, reg);
                    adapter.removeItem(position);
                    Log.d("-","Terminando");

                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if(dX > 0){
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete_white);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    } else {
                        p.setColor(Color.parseColor("#388E3C"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete_white);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
    private void removeView(){
        if(view.getParent()!=null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }


}
