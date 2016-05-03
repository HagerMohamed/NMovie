package com.example.newmovie.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.example.newmovie.Change;
import com.example.newmovie.Details.Movie;
import com.example.newmovie.R;

public class MainActivity extends AppCompatActivity implements Change{

    boolean tablet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frame);

        if ( frameLayout == null){
            tablet = false;
        }else {
            tablet = true;
        }

    }

    @Override
    public void changeFragment (Movie m){

        if (tablet ){
            SecondActivityFragment secondActivityFragment = new SecondActivityFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("movie", m);
            secondActivityFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.frame, secondActivityFragment)
                    .commit();

        } else {
            Bundle bundle = new Bundle();
            bundle.putParcelable("movie", m);
            Intent intent = new Intent(MainActivity.this ,SecondActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);

        }
    }
}
