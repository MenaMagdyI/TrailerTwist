package mina.com.trailertwist.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import mina.com.trailertwist.data.FavoriteContract;
import mina.com.trailertwist.model.Movie;
import mina.com.trailertwist.model.MovieReview;
import mina.com.trailertwist.model.MovieTrailer;

/**
 * Created by Mena on 12/14/2017.
 */

public class ProviderQueryUtils {
    private ProviderQueryUtils() {
    }

    public static List<Movie> extractMoviesFromCursor(Cursor cursor) {
        ArrayList<Movie> movies = new ArrayList<>();

        int movieId;
        String movieTitle;
        String releaseDate;
        String plot;
        String rate;
        Double rateDouble;
        String poster;

        while (cursor.moveToNext()) {
            movieId = cursor.getInt(cursor.getColumnIndex(FavoriteContract.MovieEntry._ID));
            movieTitle = cursor.getString(cursor.getColumnIndex(FavoriteContract.MovieEntry.MOVIE_TITLE));
            releaseDate = cursor.getString(cursor.getColumnIndex(FavoriteContract.MovieEntry.RELEASE_DATE));
            plot = cursor.getString(cursor.getColumnIndex(FavoriteContract.MovieEntry.PLOT));
            rate = cursor.getString(cursor.getColumnIndex(FavoriteContract.MovieEntry.RATE));
            rateDouble = Double.parseDouble(rate);
            poster = cursor.getString(cursor.getColumnIndex(FavoriteContract.MovieEntry.POSTER));


            movies.add(new Movie(movieId, movieTitle, releaseDate, plot, rateDouble, poster));
        }
        cursor.close();
        return movies;
    }

    public static List<Movie> getAllFavoriteMovies(Context context) {
        Uri queryUri = FavoriteContract.MovieEntry.CONTENT_URI;
        Cursor allFavMoviesCursor = context.getContentResolver().query(queryUri, null, null, null, null);
        return extractMoviesFromCursor(allFavMoviesCursor);
    }


    public static Uri insertFavoriteMovie(Context context, Movie movie) {
        ContentValues cv = new ContentValues();
        cv.put(FavoriteContract.MovieEntry._ID, movie.getmId());
        cv.put(FavoriteContract.MovieEntry.MOVIE_TITLE, movie.getmTitle());
        cv.put(FavoriteContract.MovieEntry.PLOT, movie.getmOverView());
        cv.put(FavoriteContract.MovieEntry.RATE, movie.getmVote());
        cv.put(FavoriteContract.MovieEntry.RELEASE_DATE, movie.getmReleaseDate());
        cv.put(FavoriteContract.MovieEntry.POSTER, movie.getmPosterPath());


        Uri insertUri = FavoriteContract.MovieEntry.CONTENT_URI;

        return context.getContentResolver().insert(insertUri, cv);
    }

    public static int deleteFavoriteMovie(Context context, int movieId) {
        Uri deleteUri = FavoriteContract.MovieEntry.CONTENT_URI
                .buildUpon()
                .appendPath(String.valueOf(movieId))
                .build();
        return context.getContentResolver().delete(deleteUri, null, null);
    }

    public static int insertFavoriteReviews(Context context, List<MovieReview> reviews, int movieId) {
        int reviewsSize = reviews.size();
        ContentValues[] values = new ContentValues[reviewsSize];
        for (int i = 0; i < reviewsSize; i++) {
            String author = reviews.get(i).getmAuthor();
            String content = reviews.get(i).getmContent();

            values[i] = new ContentValues();
            values[i].put(FavoriteContract.ReviewEntry.MOVIE_ID, movieId);
            values[i].put(FavoriteContract.ReviewEntry.AUTHOR, author);
            values[i].put(FavoriteContract.ReviewEntry.CONTENT, content);
        }
        Uri insertUri = FavoriteContract.ReviewEntry.CONTENT_URI;
        return context.getContentResolver().bulkInsert(insertUri, values);
    }

    public static ArrayList<MovieReview> getMovieReviews(Context context, int movieId) {
        Uri queryUri = FavoriteContract.ReviewEntry.CONTENT_URI;
        String selection = FavoriteContract.ReviewEntry.MOVIE_ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(movieId)};

        Cursor queryCursor = context.getContentResolver().query(queryUri, null, selection, selectionArgs, null);
        return extractReviewsFromCursor(queryCursor);
    }

    public static ArrayList<MovieReview> extractReviewsFromCursor(Cursor cursor) {
        ArrayList<MovieReview> reviews = new ArrayList<>();
        while (cursor.moveToNext()) {
            String author = cursor.getString(cursor.getColumnIndex(FavoriteContract.ReviewEntry.AUTHOR));
            String content = cursor.getString(cursor.getColumnIndex(FavoriteContract.ReviewEntry.CONTENT));

            reviews.add(new MovieReview(author, content));
        }
        cursor.close();
        return reviews;
    }

    public static int deleteMovieReviews(Context context, int movieId) {
        Uri deleteUri = FavoriteContract.ReviewEntry.CONTENT_URI;
        String where = FavoriteContract.ReviewEntry.MOVIE_ID + "=?";
        String[] whereArgs = new String[]{String.valueOf(movieId)};
        return context.getContentResolver().delete(deleteUri, where, whereArgs);
    }

    public static int insertFavoriteTrailers(Context context, List<MovieTrailer> trailers, int movieId) {
        int trailersSize = trailers.size();
        ContentValues[] values = new ContentValues[trailersSize];
        for (int i = 0; i < trailersSize; i++) {
            String title = trailers.get(i).getmName();
            String key = trailers.get(i).getmKey();

            values[i] = new ContentValues();
            values[i].put(FavoriteContract.TrailerEntry.MOVIE_ID, movieId);
            values[i].put(FavoriteContract.TrailerEntry.TITLE, title);
            values[i].put(FavoriteContract.TrailerEntry.YOUTUBE_KEY, key);
        }
        Uri insertUri = FavoriteContract.TrailerEntry.CONTENT_URI;
        return context.getContentResolver().bulkInsert(insertUri, values);
    }

    public static ArrayList<MovieTrailer> getMovieTrailers(Context context, int movieId) {
        Uri queryUri = FavoriteContract.TrailerEntry.CONTENT_URI;
        String selection = FavoriteContract.TrailerEntry.MOVIE_ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(movieId)};

        Cursor queryCursor = context.getContentResolver().query(queryUri, null, selection, selectionArgs, null);
        return extractTrailersFromCursor(queryCursor);
    }

    public static ArrayList<MovieTrailer> extractTrailersFromCursor(Cursor cursor) {
        ArrayList<MovieTrailer> trailers = new ArrayList<>();
        while (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndex(FavoriteContract.TrailerEntry.TITLE));
            String key = cursor.getString(cursor.getColumnIndex(FavoriteContract.TrailerEntry.YOUTUBE_KEY));

            trailers.add(new MovieTrailer(key, title));
        }
        cursor.close();
        return trailers;
    }

    public static int deleteMovieTrailers(Context context, int movieId) {
        Uri deleteUri = FavoriteContract.TrailerEntry.CONTENT_URI;
        String where = FavoriteContract.TrailerEntry.MOVIE_ID + "=?";
        String[] whereArgs = new String[]{String.valueOf(movieId)};
        return context.getContentResolver().delete(deleteUri, where, whereArgs);
    }
}
