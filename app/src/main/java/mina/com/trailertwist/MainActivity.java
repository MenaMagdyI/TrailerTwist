package mina.com.trailertwist;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mina.com.trailertwist.Loader.MovieLoader;
import mina.com.trailertwist.adapter.MovieAdapter;
import mina.com.trailertwist.adapter.SpinnerAdapter;
import mina.com.trailertwist.model.Movie;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Movie>>,
        SharedPreferences.OnSharedPreferenceChangeListener,
        MovieAdapter.ListItemClickListener{




    private String USGS_REQUEST_URL ;
    public static final String MOVIE_INFO = "current Movie Info";
    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private List<Movie> movieList;
    private Context mcontext = this;
    private static final int MOVIE_LOADER_ID = 1;
    private static int page = 1;
    private static String sortedBy;
    private Toast testToast;
    private Spinner spinner;
    private static int spinnerSelection;
    private final String spinner_on_save_instance_key = "spinner position" ;
    private TextView errorMessage ;
    private View loadingIndicator;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        USGS_REQUEST_URL = mcontext.getString(R.string.api_url);
        errorMessage = (TextView) findViewById(R.id.message);
        loadingIndicator = findViewById(R.id.loading_indicator);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        loadingIndicator.setVisibility(View.VISIBLE);
        movieList = new ArrayList<>();

        adapter = new MovieAdapter(this, movieList,this);



        // why is this not working ! maybe initializing spinner problem.  // [[SOLVED]]
        if(savedInstanceState != null && savedInstanceState.containsKey(spinner_on_save_instance_key)) {
            spinnerSelection = savedInstanceState.getInt(spinner_on_save_instance_key);
            Log.i("saveInstanceState ", spinnerSelection+"");
        }
        else {
            spinnerSelection = 0;
            Log.i("saveInstanceState ", spinnerSelection+"");

        }
        //Log.i("main: ","before adapter notify");

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();


        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(MOVIE_LOADER_ID, null, this);
        } else {

            errorMessage.setText(R.string.no_internet);
        }
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


        loadingIndicator.setVisibility(View.VISIBLE);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String page = sharedPrefs.getString(
                getString(R.string.api_page_url_key),
                getString(R.string.api_page_url_value));

                String temp = USGS_REQUEST_URL + sortedBy + "?" ;
        Uri baseUri = Uri.parse(temp);
        Uri.Builder uriBuilder = baseUri.buildUpon();

       // uriBuilder.appendQueryParameter(getString(R.string.api_sort_key), sortedBy);
        uriBuilder.appendQueryParameter(mcontext.getString(R.string.api_key_url_key), mcontext.getString(R.string.api_key_url_value));
        uriBuilder.appendQueryParameter(mcontext.getString(R.string.api_page_url_key), page);

        Log.i("urlllllllllllllllllll: " , uriBuilder.toString());



        return new MovieLoader(this, uriBuilder.toString(),spinnerSelection);
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movies) {


        loadingIndicator.setVisibility(View.GONE);
        movieList.clear();


        if (movies != null && !movies.isEmpty()) {

           //Log.i("on load finished " , "addeddddddddddddddd");
            movieList.addAll(movies);
            //Log.i("firstelementonmoveelist" , movieList.get(0).getmTitle());
            adapter.notifyDataSetChanged();
            errorMessage.setVisibility(View.GONE);
        }

        if (movies.size() == 0){
            recyclerView.setVisibility(View.GONE);
        }
        else if (movies.size() > 0){
            recyclerView.setVisibility(View.VISIBLE);
        }



    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
       //Log.i("on load reset " , "addeddddddddddddddd");
        movieList.clear();
        adapter.notifyDataSetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        MenuItem item = menu.findItem(R.id.sort_spinner);
        spinner = (Spinner) MenuItemCompat.getActionView(item);
        String[] arrayOfOptions = getResources().getStringArray(R.array.sort_spinner);
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(MainActivity.this, arrayOfOptions);
        spinner.setAdapter(spinnerAdapter);
        spinner.setSelection(spinnerSelection);
        setupSpinner();
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
        if (s.equals(getString(R.string.api_page_url_key))){
            movieList.clear();
            adapter.notifyDataSetChanged();
            getLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
            adapter.notifyDataSetChanged();
        }
    }


    public void restartLoader(){
        movieList.clear();
        adapter.notifyDataSetChanged();
        getLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(spinner_on_save_instance_key,spinner.getSelectedItemPosition());
        Log.i(spinner_on_save_instance_key, spinner.getSelectedItemPosition()+"");

    }
/*
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        spinnerSelection = savedInstanceState.getInt(spinner_on_save_instance_key);
    }*/

    private void setupSpinner() {

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.sorted_by_popular_label))) {
                        sortedBy = getString(R.string.api_url_sorted_by_popular);
                    }
                    else if (selection.equals(getString(R.string.sorted_by_top_rated_label))) {
                        sortedBy = getString(R.string.api_url_sorted_by_top_rated);
                    }
                    else if (selection.equals(getString(R.string.sorted_by_now_playing_label))) {
                        sortedBy = getString(R.string.api_url_sorted_by_now_playing);
                    }
                    else if (selection.equals(getString(R.string.sorted_by_now_upcoming_label))) {
                        sortedBy = getString(R.string.api_url_sorted_by_upcoming);
                    }
                    else {
                        // for favorite option in stage 2...
                    }
                   // Log.i("spinnerpositionlistner11",position+"");
                    spinnerSelection = spinner.getSelectedItemPosition();
                   // Log.i("spinnerpositionlistner22",spinnerSelection+"");
                    restartLoader();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.i("no thingggg","nothing selected !");
                sortedBy = getString(R.string.api_url_sorted_by_popular);
            }
        });
    }

    @Override
    public void onListitemClick(int clickitemIndex) {
        Movie currentMovie = movieList.get(clickitemIndex);
        Intent detailIntent = new Intent(MainActivity.this, MovieDetailsActivity.class);
        detailIntent.putExtra(MOVIE_INFO, currentMovie);
        startActivity(detailIntent);
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

