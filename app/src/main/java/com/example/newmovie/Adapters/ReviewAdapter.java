package com.example.newmovie.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.newmovie.R;
import com.example.newmovie.Details.ReviewDetails;

import java.util.ArrayList;


public class ReviewAdapter extends BaseAdapter{

    Context con;
    ArrayList<ReviewDetails> reviewArr;

    public ReviewAdapter(Context con,ArrayList<ReviewDetails> reviewArr) {
        this.con = con;
        this.reviewArr = reviewArr;
    }

    @Override
    public int getCount() {
        return reviewArr.size();
    }

    @Override
    public Object getItem(int position) {
        return reviewArr.get(position);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v;
        LayoutInflater inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.item_review, parent, false);
        TextView author = (TextView) v.findViewById(R.id.author_textt);
        TextView content = (TextView) v.findViewById(R.id.con_text);
        author.setText("author " + reviewArr.get(position).getAuthor());
        content.setText("content" + reviewArr.get(position).getCont());
        return v;
    }

}
