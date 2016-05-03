package com.example.newmovie.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.newmovie.Adapters.ReviewAdapter;
import com.example.newmovie.Adapters.TrailerAdapter;
import com.example.newmovie.DataBase.DBHelper;
import com.example.newmovie.Details.Movie;
import com.example.newmovie.Details.ReviewDetails;
import com.example.newmovie.Details.TrailersDetail;
import com.example.newmovie.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class SecondActivityFragment extends Fragment {

    ImageView im ;
    TextView title;
    TextView over_view;
    TextView vote;
    Movie movie;
    long id;
    ToggleButton fav;
    LinearLayout reviewList;
    LinearLayout trailerList;
    ReviewAdapter revAdapter;
    TrailerAdapter traiAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);
        Configuration configuration = getResources().getConfiguration();

        if (configuration.smallestScreenWidthDp >= 600) {
            Bundle b = getArguments();
            movie = (Movie) b.getParcelable("movie");
        } else {

            Intent intent = getActivity().getIntent();
            if (intent.hasExtra("movie")) {
                if (intent.getExtras().get("movie") != null) {
                    movie = (Movie) intent.getExtras().getParcelable("movie");
                }
            }
        }

        im = (ImageView) view.findViewById(R.id.img_view);
        title = (TextView) view.findViewById(R.id.title_txt);
        over_view = (TextView) view.findViewById(R.id.over_view_txt);
        vote = (TextView) view.findViewById(R.id.rate_txt);
        TextView release_date= (TextView) view.findViewById(R.id.rel_date);

        release_date.append(movie.getRelease_date());
        id =  movie.getId();

        title.setText(movie.getOriginal_title());
        over_view.append("\n" + movie.getOverview());
        vote.append("\n"+movie.getVote_average() + "");
        vote.setText((movie.getVote_average() + ""));
        Picasso.with(getActivity()).load(movie.getPoster_path()).into(im);
        reviewList = (LinearLayout) view.findViewById(R.id.rev_list);
        trailerList = (LinearLayout) view.findViewById(R.id.trailer_list);
        upgrade();

        fav = (ToggleButton) view.findViewById(R.id.favourite_btn);
        final SharedPreferences prf = getActivity().getSharedPreferences("Favourite", 0);
        final SharedPreferences.Editor handler = prf.edit();
        fav.setChecked (prf.getBoolean("Check" + movie.getId(), false));
        fav.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton btnView, boolean isCheck) {
                if (isCheck) {
                    DBHelper helper = new DBHelper(getActivity());
                    long id = helper.add_Item(movie);
                    handler.putBoolean("Check" + movie.getId(), fav.isChecked()).commit();
                } else {
                    DBHelper helper = new DBHelper(getActivity());
                    long id = helper.deleteItem(movie.getId() + "");
                    handler.putBoolean("Check" + movie.getId(), fav.isChecked());
                    handler.commit();
                }
            }
        });

        return view;
    }

    private void upgrade(){
        AsyncReview asyncReview = new AsyncReview();
        asyncReview.execute();
        AsyncTrailer asyncTrailer = new AsyncTrailer();
        asyncTrailer.execute();
    }

    class AsyncReview extends AsyncTask<Void,Integer,ArrayList<ReviewDetails>> {
        @Override
        protected ArrayList<ReviewDetails> doInBackground(Void... params) {

            String review = "Review";
            HttpURLConnection conn = null;
            BufferedReader bfReader = null;
            String jsonStr = "";
            String sid=""+id+"";

            try {

                final String BASE_URL = "http://api.themoviedb.org/3/movie/" + sid + "/reviews";
                String apiKey = "api_key";
                Uri uri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(apiKey, "d3ea2748e8ac5cb9ec538e5addcb975a").build();
                URL url = new URL(uri.toString());
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();
                InputStream input = conn.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if (input == null) {
                    return null;
                }

                bfReader = new BufferedReader(new InputStreamReader(input));
                String line;

                while ((line = bfReader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    return null;
                }
                jsonStr = buffer.toString();
                try {
                    return getReviews(jsonStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                return null;
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
                if (bfReader != null) {
                    try {
                        bfReader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }

            }
            return null;

        }

        @Override
        protected void onPostExecute (ArrayList<ReviewDetails> arr) {
            revAdapter = new ReviewAdapter(getActivity() , arr);
            for(int i=0;i<arr.size();i++) {
                View v = revAdapter.getView(i,null,null);
                reviewList.addView(v);
            }
            revAdapter.notifyDataSetChanged();

        }


        private ArrayList<ReviewDetails> getReviews (String jsonResult)throws JSONException
        {
            JSONObject jsonObject = new JSONObject(jsonResult);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            ArrayList<ReviewDetails> revList = new ArrayList<ReviewDetails>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                String author = object.getString("author");
                String content = object.getString("content");
                ReviewDetails rev = new ReviewDetails();
                rev.setAuthor(author);
                rev.setCont(content);
                Log.e("author",rev.getAuthor());
                revList.add(rev);
            }
            return revList;
        }
    }

    class AsyncTrailer extends AsyncTask<Void,Integer,ArrayList<TrailersDetail>> {

        @Override
        protected ArrayList<TrailersDetail> doInBackground(Void... params) {

            String action = "videos";
            HttpURLConnection connect = null;
            BufferedReader bfReader = null;
            String inf = null;

            try {

                String movie_param = "movie";
                String baseUrl = "http://api.themoviedb.org/3/";
                String apiKey = "api_key";
                String movieId = "" + id + "";
                Uri uri = Uri.parse(baseUrl).buildUpon()
                        .appendPath(movie_param).appendPath(movieId).appendEncodedPath(action)
                        .appendQueryParameter(apiKey, "d3ea2748e8ac5cb9ec538e5addcb975a").build();
                URL url = new URL(uri.toString());
                connect= (HttpURLConnection) url.openConnection();
                connect.setRequestMethod("GET");
                connect.connect();
                InputStream in = connect.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (in == null) {
                    inf = null;
                }
                bfReader = new BufferedReader(new InputStreamReader(in));
                String line;
                while((line = bfReader.readLine())!=null){
                    buffer.append(line+"\n");
                }
                if (buffer.length() == 0) {
                    inf = null;
                }
                inf  =buffer.toString();

            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                inf = null;
            }
            finally {
                if (connect != null) {
                    connect.disconnect();
                }
                if (bfReader != null) {
                    try {
                        bfReader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }

                try {
                    return  getTrailer(inf);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<TrailersDetail> arr) {
            traiAdapter = new TrailerAdapter(getActivity(),arr);
            for(int i=0; i<arr.size();i++) {
                View v = traiAdapter.getView(i, null, null);
                trailerList.addView(v);
            }
            traiAdapter.notifyDataSetChanged();
        }

    }

    private ArrayList<TrailersDetail> getTrailer (String json)throws JSONException{
        JSONObject jsonObject = new JSONObject(json);
        JSONArray jsonArr = jsonObject.getJSONArray("results");
        ArrayList<TrailersDetail> traiList = new ArrayList<TrailersDetail>();
        for (int i=0;i< jsonArr.length(); i++){
            JSONObject obj = jsonArr.getJSONObject(i);
            String name = obj.getString("name");
            String key = obj.getString("key");
            TrailersDetail trai = new TrailersDetail();
            trai.setName(name);
            trai.setKey(key);
            traiList.add(trai);
        }
        return traiList;
    }
}
