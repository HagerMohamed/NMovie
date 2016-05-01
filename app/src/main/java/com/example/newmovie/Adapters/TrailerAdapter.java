package com.example.newmovie.Adapters;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.newmovie.R;
import com.example.newmovie.Details.TrailersDetail;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TrailerAdapter extends BaseAdapter{

    Context conn;
    ArrayList <TrailersDetail> trailerArr;
    String key;

    public TrailerAdapter(Context conn ,ArrayList <TrailersDetail> trailerArr){
        this.conn = conn;
        this.trailerArr = trailerArr;
    }


    @Override
    public int getCount() {
        return trailerArr.size();
    }

    @Override
    public Object getItem(int position) {
        return trailerArr.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v;
        LayoutInflater inflater = (LayoutInflater) conn.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.item_trailer, parent, false);

        ImageView im = (ImageView) v.findViewById(R.id.trailer_im);
        TextView trailersName = (TextView) v.findViewById(R.id.trailer_name);
        String base = "http://img.youtube.com/vi/";
        key = trailerArr.get(position).getKey();
        String extension="/default.jpg";
        Picasso.with(conn).load(base+key+extension).into(im);
        trailersName.setText(trailerArr.get(position).getName());
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url ="https://www.youtube.com/watch?v=" + key;
                conn.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });
        return v;

    }
}
