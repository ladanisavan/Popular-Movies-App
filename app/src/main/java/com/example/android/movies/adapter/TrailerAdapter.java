package com.example.android.movies.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.android.movies.R;
import com.example.android.movies.model.Trailer;
import com.example.android.movies.util.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by savan on 28/12/2015.
 */
public class TrailerAdapter extends ArrayAdapter<Trailer> {

    private final String LOG_TAG = TrailerAdapter.class.getSimpleName();

    public TrailerAdapter(Context context, List<Trailer> trailers) {
        super(context, 0, trailers);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Trailer trailer = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.trailer_list_item, parent, false);
        }

        ImageView mContentView = (ImageView) convertView.findViewById(R.id.movie_detail_trailer);
        Picasso.with(getContext())
                .load(Utils.generateYoutubeMQThumbnail(trailer.getKey()))
                .fit()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.ic_broken_image_black_24dp)
                .into(mContentView);

        ImageView mPlayButton = (ImageView)convertView.findViewById(R.id.play_button);
        convertView.setOnClickListener(generateOnItemClickListener(trailer));
        mPlayButton.setOnClickListener(generateOnItemClickListener(trailer));

        return convertView;
    }


    private View.OnClickListener generateOnItemClickListener(final Trailer trailer)  {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent youtubeIntent = new Intent(Intent.ACTION_VIEW);
                youtubeIntent.setData(Utils.generateYoutubeUri(trailer.getKey()));
                getContext().startActivity(youtubeIntent);
            }
        };
    }
}
