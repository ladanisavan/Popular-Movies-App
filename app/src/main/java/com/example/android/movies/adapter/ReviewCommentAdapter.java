package com.example.android.movies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.movies.R;
import com.example.android.movies.model.Review;

import java.util.List;

/**
 * Created by savan on 28/12/2015.
 */
public class ReviewCommentAdapter extends ArrayAdapter<Review> {

    private final String LOG_TAG = ReviewCommentAdapter.class.getSimpleName();

    public ReviewCommentAdapter(Context context, List<Review> reviews) {
        super(context, 0, reviews);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Review review = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.review_list_item, parent, false);
        }

        TextView authorNameView = (TextView) convertView.findViewById(R.id.author_name);
        authorNameView.setText(review.getAuthor());
        TextView reviewContentView = (TextView) convertView.findViewById(R.id.review_content);
        reviewContentView.setText(review.getContent());

        return convertView;
    }
}
