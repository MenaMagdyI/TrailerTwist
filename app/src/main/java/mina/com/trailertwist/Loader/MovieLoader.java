package mina.com.trailertwist.Loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

import mina.com.trailertwist.model.Movie;
import mina.com.trailertwist.utils.NetworkQueryUtils;

/**
 * Created by Mena on 11/19/2017.
 */

public class MovieLoader extends AsyncTaskLoader<List<Movie>> {

    private static final String LOG_TAG = MovieLoader.class.getName();
    private String mUrl;

    public MovieLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Movie> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

       //Log.i("before fetch data",mUrl);
        List<Movie> movies = NetworkQueryUtils.fetchDatafMovies(mUrl,getContext());
       // Log.i("name: " , movies.get(0).getmTitle());
        return movies;
    }
}
