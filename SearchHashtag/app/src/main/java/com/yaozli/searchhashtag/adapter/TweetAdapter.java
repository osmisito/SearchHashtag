package com.yaozli.searchhashtag.adapter;


import java.util.ArrayList;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yaozli.searchhashtag.R;
import com.yaozli.searchhashtag.dto.TweetDTO;


public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder>{
    private ArrayList<TweetDTO> mDataset;
    private Context ctx;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtUsername;
        public ImageView imgUser;
        public TextView txtContent;


        public ViewHolder(View v) {
            super(v);
            txtUsername = (TextView) v.findViewById(R.id.username);
            imgUser = (ImageView) v.findViewById(R.id.imgUser);
            txtContent = (TextView) v.findViewById(R.id.contentTweet);

        }
    }
    public void removeItem(int position) {
        mDataset.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mDataset.size());
    }


    public void add(int position, TweetDTO item) {
        mDataset.add(position, item);
        notifyItemInserted(position);
    }

    public TweetDTO getItem(int position){
        return mDataset.get(position);

    }

    public void remove(TweetDTO item) {
        int position = mDataset.indexOf(item);
        mDataset.remove(position);
        notifyItemRemoved(position);
    }

    public TweetAdapter(ArrayList<TweetDTO> myDataset, Context ctx) {
        mDataset = myDataset;
        this.ctx = ctx;

    }



    // Create new views (invoked by the layout manager)
    @Override
    public TweetAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tweet, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.txtContent.setText(mDataset.get(position).getContent());
        holder.txtUsername.setText(mDataset.get(position).getUsername());
        holder.imgUser.setImageBitmap(mDataset.get(position).getImage());


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
