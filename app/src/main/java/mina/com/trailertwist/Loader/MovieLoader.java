package mina.com.trailertwist.Loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

import mina.com.trailertwist.model.Movie;
import mina.com.trailertwist.utils.NetworkQueryUtils;
import mina.com.trailertwist.utils.ProviderQueryUtils;

/**
 * Created by Mena on 11/19/2017.
 */

public class MovieLoader extends AsyncTaskLoader<List<Movie>> {

    private static final String LOG_TAG = MovieLoader.class.getName();
    private String mUrl;
    private int mUserChoice;

    public MovieLoader(Context context, String url,int userChoice) {
        super(context);
        mUrl = url;
        mUserChoice = userChoice;
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
        List<Movie> movies;
       Log.i("user choiceeee",Integer.toString(mUserChoice));

        if (mUserChoice == 4){
            movies = ProviderQueryUtils.getAllFavoriteMovies(getContext());
        }
        else{
            movies = NetworkQueryUtils.fetchDatafMovies(mUrl,getContext());
        }

       // Log.i("Movies size of the DB: " ,Integer.toString( movies.size()));
        return movies;
    }
}
