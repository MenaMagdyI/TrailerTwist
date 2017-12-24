package mina.com.trailertwist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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


public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Movie>>,
        SharedPreferences.OnSharedPreferenceChangeListener,
        MovieAdapter.ListItemClickListener,
        MovieAdapter.OnReachLastPosition {

    public MainFragment() {
        setHasOptionsMenu(true);
    }

    private String USGS_REQUEST_URL;
    public static final String MOVIE_INFO = "current Movie Info";
    private StatefulRecyclerView recyclerView;
    private MovieAdapter adapter;
    private ArrayList<Movie> movieList = new ArrayList<>();
    private static final int MOVIE_LOADER_ID = 1;
    private static int page = 1;
    private static String sortedBy;
    private Toast testToast;
    private Spinner spinner;
    private static int spinnerSelection;
    private final String spinner_on_save_instance_key = "spinner position";
    private TextView errorMessage;
    private View loadingIndicator;
    private NetworkInfo networkInfo;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        USGS_REQUEST_URL = getString(R.string.api_url);
        errorMessage = (TextView) rootView.findViewById(R.id.message);
        loadingIndicator = rootView.findViewById(R.id.loading_indicator);
        recyclerView = (StatefulRecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        loadingIndicator.setVisibility(View.VISIBLE);
        movieList = new ArrayList<>();

        adapter = new MovieAdapter(getActivity(), this);



        onOrientationChange(getResources().getConfiguration().orientation);


        if (savedInstanceState != null && savedInstanceState.containsKey(spinner_on_save_instance_key)) {
            spinnerSelection = savedInstanceState.getInt(spinner_on_save_instance_key);
            Log.i("saveInstanceState ", spinnerSelection + "");
        } else {
            spinnerSelection = 0;
            Log.i("saveInstanceState ", spinnerSelection + "");

        }

        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connMgr.getActiveNetworkInfo();


        if (savedInstanceState == null) {
            if (networkInfo != null && networkInfo.isConnected()) {
                Loader<List<Movie>> loaderrmaneger = getLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);
            } else {

                errorMessage.setText(R.string.no_internet);
            }
        }


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        prefs.registerOnSharedPreferenceChangeListener(this);


        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter.notifyDataSetChanged();

        recyclerView.setAdapter(adapter);


        return rootView;

    }

    public void onOrientationChange(int orientation) {
        int landScape = 3;
        int portrait = 2;
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;
       /* if (widthPixels >= 1023 || heightPixels >= 1023) {
            landScape = 4;
            portrait = 3;
        }*/

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            mLayoutManager = new GridLayoutManager(getContext(), portrait);
            recyclerView.setLayoutManager(mLayoutManager);
            adapter = new MovieAdapter(getContext(), this);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);

        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mLayoutManager = new GridLayoutManager(getActivity(), landScape);
            recyclerView.setLayoutManager(mLayoutManager);
            adapter = new MovieAdapter(getActivity(), this);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
        }
    }


    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {

        loadingIndicator.setVisibility(View.VISIBLE);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String page = sharedPrefs.getString(
                getString(R.string.api_page_url_key),
                getString(R.string.api_page_url_value));

        String temp = USGS_REQUEST_URL + sortedBy + "?";
        Uri baseUri = Uri.parse(temp);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter(getString(R.string.api_key_url_key), getString(R.string.api_key_url_value));
        uriBuilder.appendQueryParameter(getString(R.string.api_page_url_key), page);


        return new MovieLoader(getActivity(), uriBuilder.toString(), spinnerSelection);
    }


    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movies) {
        loadingIndicator.setVisibility(View.GONE);
        movieList.clear();


        if (movies != null && !movies.isEmpty()) {
            movieList.addAll(movies);
            adapter.setDataList(movieList);
            errorMessage.setVisibility(View.GONE);
        }

        if (movies.size() == 0) {
            recyclerView.setVisibility(View.GONE);
        } else if (movies.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        movieList.clear();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.mainmenu, menu);
        MenuItem item = menu.findItem(R.id.sort_spinner);
        spinner = (Spinner) MenuItemCompat.getActionView(item);
        String[] arrayOfOptions = getResources().getStringArray(R.array.sort_spinner);
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(getActivity(), arrayOfOptions);
        spinner.setAdapter(spinnerAdapter);
        spinner.setSelection(spinnerSelection);
        setupSpinner();
        super.onCreateOptionsMenu(menu, inflater);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Log.i("before setting activity", "111111111111111");
            Intent settingsIntent = new Intent(getActivity(), SettingActivity.class);
            startActivity(settingsIntent);
            Log.i("After setting activity", "2222222222222222");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals(getString(R.string.api_page_url_key))) {
            movieList.clear();
            adapter.notifyDataSetChanged();
            getLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
            adapter.notifyDataSetChanged();
        }
    }


    public void restartLoader() {
        movieList.clear();
        adapter.notifyDataSetChanged();
        getLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(spinner_on_save_instance_key, spinner.getSelectedItemPosition());
        outState.putParcelableArrayList("myMoviesList", movieList);
        Log.i(spinner_on_save_instance_key, spinner.getSelectedItemPosition() + "");
    }


    private void setupSpinner() {

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.sorted_by_popular_label))) {
                        sortedBy = getString(R.string.api_url_sorted_by_popular);
                    } else if (selection.equals(getString(R.string.sorted_by_top_rated_label))) {
                        sortedBy = getString(R.string.api_url_sorted_by_top_rated);
                    } else if (selection.equals(getString(R.string.sorted_by_now_playing_label))) {
                        sortedBy = getString(R.string.api_url_sorted_by_now_playing);
                    } else if (selection.equals(getString(R.string.sorted_by_now_upcoming_label))) {
                        sortedBy = getString(R.string.api_url_sorted_by_upcoming);
                    } else {
                        // for favorite option in stage 2...
                    }

                    spinnerSelection = spinner.getSelectedItemPosition();
                    restartLoader();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                sortedBy = getString(R.string.api_url_sorted_by_popular);
            }
        });
    }

    @Override
    public void onListitemClick(int clickitemIndex) {
        Movie currentMovie = movieList.get(clickitemIndex);
        Intent detailIntent = new Intent(getActivity(), MovieDetailsActivity.class);
        detailIntent.putExtra(MOVIE_INFO, currentMovie);
        startActivity(detailIntent);
    }


    // not working --- IN PROGRESS
    @Override
    public void refreshPage(int p) {
        if (!sortedBy.equals(getString(R.string.sorted_by_favorites_lable)) && networkInfo.isConnected()) {
            page++;
            restartLoader();
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



