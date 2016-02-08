package com.example.android.movies.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.movies.R;
import com.example.android.movies.adapter.PagerAdapter;
import com.example.android.movies.fragment.MovieDetailActivityFragment;
import com.example.android.movies.fragment.RenderMovieGridFragment;
import com.example.android.movies.model.MovieDetail;
import com.example.android.movies.util.Constants;

public class MainActivity extends AppCompatActivity implements RenderMovieGridFragment.Callback{

    private boolean mTwoPane = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(findViewById(R.id.movie_detail_container) != null){
            mTwoPane = true;
        }else{
            mTwoPane = false;
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(MovieDetail movieDetail) {
        if(mTwoPane){
            Bundle args = new Bundle();
            args.putParcelable(Constants.MOVIE_DETAIL_KEY,movieDetail);

            MovieDetailActivityFragment fragment = new MovieDetailActivityFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment)
                    .commit();
        }else{
            Intent intent = new Intent(this,MovieDetailActivity.class)
                    .putExtra(Intent.EXTRA_TEXT, movieDetail);
            startActivity(intent);
        }
    }
}

