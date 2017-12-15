package mina.com.trailertwist.Loader;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

import mina.com.trailertwist.model.MovieTrailer;
import mina.com.trailertwist.utils.NetworkQueryUtils;

/**
 * Created by Mena on 12/9/2017.
 */

public class TrailerLoader extends AsyncTaskLoader<List<MovieTrailer>> {
    private static final String LOG_TAG = MovieLoader.class.getName();
    private String mUrl;

    public TrailerLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }


    @Override
    public List<MovieTrailer> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        List<MovieTrailer> trailers = NetworkQueryUtils.fetchDatafTrailers(mUrl, getContext());
        return trailers;
    }
}
