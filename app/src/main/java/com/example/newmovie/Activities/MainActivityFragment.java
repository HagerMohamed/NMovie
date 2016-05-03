package com.example.newmovie.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.newmovie.Adapters.ImageAdapter;
import com.example.newmovie.Change;
import com.example.newmovie.DataBase.DBHelper;
import com.example.newmovie.Details.Movie;
import com.example.newmovie.R;

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

public class MainActivityFragment extends Fragment {
    boolean isPopular = true;
    GridView gv;
    ArrayList<String> paths=new ArrayList<String>();
    ArrayList<Movie> movie = new ArrayList<Movie>();
    ImageAdapter ima;
    Change change;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, null);
        gv = (GridView) v.findViewById(R.id.grid_view);
        SharedPreferences prf= getActivity().getSharedPreferences("test", 0);
        isPopular = prf.getBoolean("order",true);

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                
                Movie m = movie.get(position);
                change.changeFragment(m);
            }
        });
        update();
        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        change= (Change) context;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences prf = getActivity().getSharedPreferences("test", 0);
        SharedPreferences.Editor handler = prf.edit();
        int id = item.getItemId();
        if (id == R.id.action_fav){
            DBHelper dbHelper = new DBHelper(getActivity());
            movie = dbHelper.getItem();
            paths = new ArrayList<String>();
            for(Movie m : movie){
                paths.add(m.getPoster_path());
            }
            ima = new ImageAdapter(getActivity(), paths);
            gv.setAdapter(ima);
        } else if (id == R.id.action_sort_by_popularity){
             isPopular = true;
             handler.putBoolean("order",true).commit();
             update();
        } else {
            isPopular = false;
            handler.putBoolean("order",false).commit();
            update();
        }
        return super.onOptionsItemSelected(item);
    }



    public void update(){
        Async fetch = new Async();
        fetch.execute(isPopular);
    }

    public class Async extends AsyncTask <Boolean,Void,ArrayList<Movie>>
    {

        @Override
        protected ArrayList<Movie> doInBackground(Boolean... params) {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String sortOrder = pref.getString(getString(R.string.prf_sorted_key)
                    , getString((R.string.prf_sorted_highest_rated)));

            if (sortOrder.equals(getString(R.string.favourite_txt))) {
                DBHelper dbHelper = new DBHelper(getActivity());
                movie = dbHelper.getItem();
                if (movie == null) {
                    Toast.makeText(getActivity(), "No Favorite Films", Toast.LENGTH_LONG).show();
                    return null;
                } else {
                    return movie;
                }
            } else {


                HttpURLConnection uConnection = null;
                BufferedReader reader = null;
                String jsonStr = "";
                if (paths != null) {
                    paths.clear();
                    movie.clear();
                }

                try {
                    URL url;

                    if (isPopular) {
                        url = new URL("http://api.themoviedb.org/3/movie/popular?api_key=d3ea2748e8ac5cb9ec538e5addcb975a");
                    } else {
                        url = new URL("http://api.themoviedb.org/3/movie/top_rated?api_key=d3ea2748e8ac5cb9ec538e5addcb975a");
                    }
                    uConnection = (HttpURLConnection) url.openConnection();
                    uConnection.setRequestMethod("GET");
                    uConnection.connect();

                    InputStream inputStream = uConnection.getInputStream();
                    StringBuffer buffer = new StringBuffer();
                    if (inputStream == null) {
                        return null;
                    }

                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;

                    while ((line = reader.readLine()) != null) {
                        buffer.append(line + "\n");
                    }

                    if (buffer.length() == 0) {
                        return null;
                    }

                    jsonStr = buffer.toString();
                    try {
                        getJason(jsonStr);
                        return movie;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } catch (IOException e) {
                    Log.e("PlaceholderFragment", "Error ", e);
                    return null;
                } finally {
                    if (uConnection != null) {
                        uConnection.disconnect();
                    }
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (final IOException e) {
                            Log.e("PlaceholderFragment", "Error closing stream", e);
                        }
                    }

                    return null;

                }
            }
            }

            @Override
            protected void onPostExecute (ArrayList < Movie > movies) {
                super.onPostExecute(movies);
                ima = new ImageAdapter(getActivity(), paths);
                gv.setAdapter(ima);
            }

    }




    private ArrayList<String> getJason (String str) throws JSONException {
        JSONObject jObj = new JSONObject(str);
        JSONArray jArr = jObj.getJSONArray("results");
        for (int i = 0; i < jArr.length(); i++){
            Movie m = new Movie();
            JSONObject j = jArr.getJSONObject(i);
            String poster = "http://image.tmdb.org/t/p/w185/" +j.getString("poster_path");
            String title = j.getString("original_title");
            String over_view = j.getString("overview");
            String release_date = j.getString("release_date");
            double  vote_average = j.getDouble("vote_average");
            long id=j.getLong("id");

            m.setOriginal_title(title);
            m.setOverview(over_view);
            m.setPoster_path(poster);
            m.setRelease_date(release_date);
            m.setVote_average(vote_average);
            m.setId(id);
            movie.add(m);
            paths.add(poster);
        }
        return  paths;
    }
}
