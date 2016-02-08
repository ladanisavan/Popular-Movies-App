package com.example.android.movies.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.android.movies.R;
import com.example.android.movies.fragment.FavoriteMovieGridFragment;
import com.example.android.movies.fragment.RenderMovieGridFragment;
import com.example.android.movies.util.Constants;

/**
 * Created by savan on 05/02/2016.
 */
public class PagerAdapter extends FragmentPagerAdapter {

    Context mContext;

    public PagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                RenderMovieGridFragment popularFragment = new RenderMovieGridFragment();
                Bundle popularFgmtBdle = new Bundle();
                popularFgmtBdle.putString(Constants.SORT_BY_KEY, mContext.getString(R.string.pref_sort_by_popularity));
                popularFragment.setArguments(popularFgmtBdle);
                return popularFragment;
            case 1:
                RenderMovieGridFragment topRatedFragment = new RenderMovieGridFragment();
                Bundle topRatedFgmtBdle = new Bundle();
                topRatedFgmtBdle.putString(Constants.SORT_BY_KEY, mContext.getString(R.string.pref_sort_by_rating));
                topRatedFragment.setArguments(topRatedFgmtBdle);
                return topRatedFragment;
            case 2:
                return new FavoriteMovieGridFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return Constants.TAB_POPULAR;
            case 1:
                return Constants.TAB_TOP_RATED;
            case 2:
                return Constants.TAB_FAVORITE;
            default:
                return null;
        }
    }
}
