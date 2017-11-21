package mina.com.trailertwist;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import mina.com.trailertwist.Loader.MovieLoader;
import mina.com.trailertwist.adapter.MovieAdapter;
import mina.com.trailertwist.model.Movie;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Movie>>,
        SharedPreferences.OnSharedPreferenceChangeListener{




    private String USGS_REQUEST_URL ;
    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private List<Movie> movieList;
    private Context mcontext = this;
    private static final int EARTHQUAKE_LOADER_ID = 1;
    private static int page = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        USGS_REQUEST_URL = mcontext.getString(R.string.api_url);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        movieList = new ArrayList<>();

        adapter = new MovieAdapter(this, movieList);
        //Log.i("main: ","before adapter notify");
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
/*
        movieList.add(new Movie(5.6, "It","/9E2y5Q7WlCVNEhP5GiVTjhEhx1o.jpg"));
        movieList.add(new Movie(9.3, "Batman returns","/9E2y5Q7WlCVNEhP5GiVTjhEhx1o.jpg"));
        movieList.add(new Movie(5.6, "It","/9E2y5Q7WlCVNEhP5GiVTjhEhx1o.jpg"));
        movieList.add(new Movie(9.3, "Batman returns","/9E2y5Q7WlCVNEhP5GiVTjhEhx1o.jpg"));
        movieList.add(new Movie(5.6, "It","/9E2y5Q7WlCVNEhP5GiVTjhEhx1o.jpg"));
        movieList.add(new Movie(9.3, "Batman returns","/9E2y5Q7WlCVNEhP5GiVTjhEhx1o.jpg"));
        movieList.add(new Movie(5.6, "It","/9E2y5Q7WlCVNEhP5GiVTjhEhx1o.jpg"));
        movieList.add(new Movie(9.3, "Batman returns","/9E2y5Q7WlCVNEhP5GiVTjhEhx1o.jpg"));

*/

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);

      // Log.i("main: ","after adapter notify");
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        //recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter.notifyDataSetChanged();

        recyclerView.setAdapter(adapter);




    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String sortedBy = sharedPrefs.getString(
                getString(R.string.api_sort_key),
                getString(R.string.api_url_sorted_by_top_rated));

                String temp = USGS_REQUEST_URL + sortedBy + "?" ;
        Uri baseUri = Uri.parse(temp);
        Uri.Builder uriBuilder = baseUri.buildUpon();

       // uriBuilder.appendQueryParameter(getString(R.string.api_sort_key), sortedBy);
        uriBuilder.appendQueryParameter(mcontext.getString(R.string.api_key_url_key), mcontext.getString(R.string.api_key_url_value));
        uriBuilder.appendQueryParameter(mcontext.getString(R.string.api_page_url_key), Integer.toString(page));

        Log.i("urlllllllllllllllllll: " , uriBuilder.toString());


        return new MovieLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movies) {



        if (movies != null && !movies.isEmpty()) {
            movieList.addAll(movies);
            //Log.i("firstelementonmoveelist" , movieList.get(0).getmTitle());
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        movieList.clear();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Log.i("before setting activity","111111111111111");
            Intent settingsIntent = new Intent(this, SettingActivity.class);
            startActivity(settingsIntent);
            Log.i("After setting activity","2222222222222222");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals(getString(R.string.api_sort_key))){
            movieList.clear();
            adapter.notifyDataSetChanged();
            getLoaderManager().restartLoader(EARTHQUAKE_LOADER_ID, null, this);
            adapter.notifyDataSetChanged();
        }
    }


    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}

