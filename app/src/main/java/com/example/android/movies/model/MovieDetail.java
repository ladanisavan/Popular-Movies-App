package com.example.android.movies.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.android.movies.fragment.FavoriteMovieGridFragment;
import com.google.gson.annotations.SerializedName;

/**
 * Created by savan on 28/12/2015.
 * This class holds detail of movie.
 */
public class MovieDetail implements Parcelable{

    @SerializedName("id")
    private int id;
    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("title")
    private String title;
    @SerializedName("popularity")
    private float popularity;
    @SerializedName("vote_count")
    private long voteCount;
    @SerializedName("vote_average")
    private float voteAvg;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("overview")
    private String movieOverview;
    private boolean isFavorite;

    public MovieDetail(){

    }

    public MovieDetail(int id, String posterPath, String title, float popularity, long voteCount, float voteAvg, String releaseDate, String movieOverview) {
        this.id = id;
        this.posterPath = posterPath;
        this.title = title;
        this.popularity = popularity;
        this.voteCount = voteCount;
        this.voteAvg = voteAvg;
        this.releaseDate = releaseDate;
        this.movieOverview = movieOverview;
    }

    public MovieDetail(int id, String posterPath, String title) {
        this.id = id;
        this.posterPath = posterPath;
        this.title = title;
    }

    public MovieDetail(Cursor cursor) {
        this.id = cursor.getInt(FavoriteMovieGridFragment.COL_MOVIE_ID);
        this.title = cursor.getString(FavoriteMovieGridFragment.COL_TITLE);
        this.posterPath = cursor.getString(FavoriteMovieGridFragment.COL_IMAGE);
        //this.image2 = cursor.getString(FavoriteMovieGridFragment.COL_IMAGE2);
        this.movieOverview = cursor.getString(FavoriteMovieGridFragment.COL_OVERVIEW);
        this.voteAvg = cursor.getInt(FavoriteMovieGridFragment.COL_RATING);
        this.releaseDate = cursor.getString(FavoriteMovieGridFragment.COL_DATE);
    }

    public MovieDetail(Parcel parcel) {
        this.id = parcel.readInt();
        this.posterPath = parcel.readString();
        this.title = parcel.readString();
        this.popularity = parcel.readFloat();
        this.voteCount = parcel.readLong();
        this.voteAvg = parcel.readFloat();
        this.releaseDate = parcel.readString();
        this.movieOverview = parcel.readString();
        this.isFavorite = parcel.readByte() != 0;
    }

    public int getId() {
        return id;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getTitle() {
        return title;
    }

    public float getPopularity() {
        return popularity;
    }

    public long getVoteCount() {
        return voteCount;
    }

    public float getVoteAvg() {
        return voteAvg;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPopularity(float popularity) {
        this.popularity = popularity;
    }

    public void setVoteCount(long voteCount) {
        this.voteCount = voteCount;
    }

    public void setVoteAvg(float voteAvg) {
        this.voteAvg = voteAvg;
    }

    public String getReleaseDate() { return releaseDate; }

    public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }

    public String getMovieOverview() {
        return movieOverview;
    }

    public void setMovieOverview(String movieOverview) {
        this.movieOverview = movieOverview;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    @Override
    public String toString() {
        return "MovieDetail{" +
                "id=" + id +
                ", posterPath='" + posterPath + '\'' +
                ", title='" + title + '\'' +
                ", popularity=" + popularity +
                ", voteCount=" + voteCount +
                ", voteAvg=" + voteAvg +
                ", releaseDate='" + releaseDate + '\'' +
                ", movieOverview='" + movieOverview + '\'' +
                ", isFavorite=" + isFavorite +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(id);
        parcel.writeString(posterPath);
        parcel.writeString(title);
        parcel.writeFloat(popularity);
        parcel.writeLong(voteCount);
        parcel.writeFloat(voteAvg);
        parcel.writeString(releaseDate);
        parcel.writeString(movieOverview);
        parcel.writeByte((byte)(isFavorite?1:0));
    }

    public final static Parcelable.Creator<MovieDetail> CREATOR = new Creator<MovieDetail>() {
        @Override
        public MovieDetail createFromParcel(Parcel parcel) {
            return new MovieDetail(parcel);
        }

        @Override
        public MovieDetail[] newArray(int size) {
            return new MovieDetail[size];
        }
    };
}
