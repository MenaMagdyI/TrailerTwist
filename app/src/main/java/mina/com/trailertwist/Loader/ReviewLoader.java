package mina.com.trailertwist.Loader;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

import mina.com.trailertwist.model.MovieReview;
import mina.com.trailertwist.utils.NetworkQueryUtils;

/**
 * Created by Mena on 12/9/2017.
 */

public class ReviewLoader extends AsyncTaskLoader<List<MovieReview>> {

    private static final String LOG_TAG = MovieLoader.class.getName();
    private String mUrl;

    public ReviewLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<MovieReview> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        List<MovieReview> reviews = NetworkQueryUtils.fetchDatafReviews(mUrl, getContext());
        return reviews;
    }
}
