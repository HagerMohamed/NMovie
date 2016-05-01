package com.example.newmovie.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.newmovie.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageAdapter  extends BaseAdapter{

    Context cn;
    ArrayList<String> paths;

    public ImageAdapter(Context cn,ArrayList<String> paths) {
        this.cn = cn;
        this.paths = paths;
    }

    @Override
    public int getCount() {
        return paths.size();
    }

    @Override
    public Object getItem(int position) {

        return paths.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imv;
        LayoutInflater inf = (LayoutInflater) cn.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inf.inflate(R.layout.image_view,null);
        imv = (ImageView) v.findViewById(R.id.grid_item);
        Picasso.with(cn).load(paths.get(position)).into(imv);
        return v;
    }
}
