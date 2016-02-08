package com.example.android.movies.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.example.android.movies.data.MovieContract;

/**
 * Created by savan on 03/02/2016.
 */
public class Utils {

    public static Uri generateYoutubeMQThumbnail(String youtubeId)  {
        return Uri.parse("http://img.youtube.com/vi/")
                .buildUpon()
                .appendEncodedPath(youtubeId)
                .appendEncodedPath("mqdefault.jpg")
                .build();
    }

    public static Uri generateYoutubeUri(String id) {
        return Uri.parse("https://www.youtube.com/watch")
                .buildUpon()
                .appendQueryParameter("v", id)
                .build();
    }

    public static int isFavorited(Context context, int id) {
        Cursor cursor = context.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                null,   // projection
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?", // selection
                new String[] { Integer.toString(id) },   // selectionArgs
                null    // sort order
        );
        int numRows = cursor.getCount();
        cursor.close();
        return numRows;
    }
}
