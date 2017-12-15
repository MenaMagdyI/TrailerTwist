package mina.com.trailertwist;



import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import mina.com.trailertwist.Loader.ReviewLoader;
import mina.com.trailertwist.Loader.TrailerLoader;
import mina.com.trailertwist.adapter.ReviewAdapter;
import mina.com.trailertwist.adapter.TrailerAdapter;
import mina.com.trailertwist.data.FavoriteContract;
import mina.com.trailertwist.model.Movie;
import mina.com.trailertwist.model.MovieReview;
import mina.com.trailertwist.model.MovieTrailer;
import mina.com.trailertwist.utils.ProviderUtils;

public class MovieDetailsActivity extends AppCompatActivity
implements TrailerAdapter.ListItemClickListener
{

            private Context mcontext = this;

            private String USGS_REQUEST_URL ;
            private Movie selectedmovie;
            private TextView mtitle, mCardTitle, mreleaseDate,mRate,mOverview,mCardRate,mCardVotesC,mCardAdult;
            private ImageView mPoster, mBackdrop;
            private RecyclerView trailersrecyclerView, reviewsrecyclerview;
            private double rate;
            private String mStrAdult = "no", testFavBut;;
            private Toast warnings;
            private boolean isFavoriteFlag;



            private ArrayList<MovieTrailer> mTrailers;
            public static final int TRAILERS_LOADER_ID = 44;
            private TrailerAdapter mTrailersAdapter;


            private ArrayList<MovieReview> mReviews;
            public static final int REVIEWS_LOADER_ID = 55;
            private ReviewAdapter mReviewAdapter;


        private LoaderCallbacks<List<MovieTrailer>> MovieTrailerCallbacks =
            new LoaderCallbacks<List<MovieTrailer>>(){
                @Override
                public Loader<List<MovieTrailer>> onCreateLoader(int id, Bundle args) {

                    String temp = USGS_REQUEST_URL + selectedmovie.getmId() + "/"+mcontext.getString(R.string.trailers_url_path)+"?";
                    Uri baseUri = Uri.parse(temp);
                    Uri.Builder uriBuilder = baseUri.buildUpon();
                    uriBuilder.appendQueryParameter(mcontext.getString(R.string.api_key_url_key), mcontext.getString(R.string.api_key_url_value));

                    Log.i("Trailers url: " , uriBuilder.toString());
                    return new TrailerLoader(mcontext, uriBuilder.toString());
                }

                @Override
                public void onLoadFinished(Loader<List<MovieTrailer>> loader, List<MovieTrailer> data) {

                    if (data != null && !data.isEmpty()){
                        mTrailers.addAll(data);
                        mTrailersAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onLoaderReset(Loader<List<MovieTrailer>> loader) {
                    mTrailers.clear();
                }

            };


        private LoaderCallbacks<List<MovieReview>>  MovieReviewCallbacks =
            new LoaderCallbacks<List<MovieReview>>(){

                @Override
                public Loader<List<MovieReview>> onCreateLoader(int i, Bundle bundle) {
                    String temp = USGS_REQUEST_URL + selectedmovie.getmId() + "/"+mcontext.getString(R.string.reviews_url_path)+"?";
                    Uri baseUri = Uri.parse(temp);
                    Uri.Builder uriBuilder = baseUri.buildUpon();
                    uriBuilder.appendQueryParameter(mcontext.getString(R.string.api_key_url_key), mcontext.getString(R.string.api_key_url_value));

                    Log.i("Reviews url: " , uriBuilder.toString());
                    return new ReviewLoader(mcontext, uriBuilder.toString());

                }

                @Override
                public void onLoadFinished(Loader<List<MovieReview>> loader, List<MovieReview> movieReviews) {

                    if (movieReviews != null && !movieReviews.isEmpty()){
                        mReviews.addAll(movieReviews);
                        mReviewAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onLoaderReset(Loader<List<MovieReview>> loader) {

                    mReviews.clear();
                }
            };





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        USGS_REQUEST_URL = mcontext.getString(R.string.api_url);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initCollapsingToolbar();


        selectedmovie = getIntent().getParcelableExtra(MainActivity.MOVIE_INFO);



        mTrailers = new ArrayList<>();
        mReviews = new ArrayList<>();
        mTrailersAdapter = new TrailerAdapter (this,mTrailers,this); // the third args is for the onclick listner
        mReviewAdapter = new ReviewAdapter(this,mReviews);

        Uri movieUri = FavoriteContract.MovieEntry.CONTENT_URI;
        final String selection = FavoriteContract.MovieEntry._ID + "=?";
        final String[] selectionArgs = new String[]{String.valueOf(selectedmovie.getmId())};
        final Cursor isFavoriteCursor = getContentResolver().query(movieUri, null, selection, selectionArgs, null);

        if (isFavoriteCursor.getCount() > 0) {
            isFavoriteFlag = true;
        } else {
            isFavoriteFlag = false;
        }
        isFavoriteCursor.close();

        MaterialFavoriteButton materialFavoriteButtonNice = (MaterialFavoriteButton) findViewById(R.id.favorite_nice);

        materialFavoriteButtonNice.setFavorite(isFavoriteFlag, !isFavoriteFlag);
        materialFavoriteButtonNice.setOnFavoriteChangeListener(
                new MaterialFavoriteButton.OnFavoriteChangeListener() {
                    @Override
                    public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                        if (favorite) {
                            ProviderUtils.insertFavoriteMovie(mcontext, selectedmovie);
                            ProviderUtils.insertFavoriteReviews(mcontext, mReviews, selectedmovie.getmId());
                            ProviderUtils.insertFavoriteTrailers(mcontext, mTrailers, selectedmovie.getmId());

                            testFavBut = "Favorite it !";
                        } else {
                            ProviderUtils.deleteFavoriteMovie(mcontext, selectedmovie.getmId());
                            ProviderUtils.deleteMovieReviews(mcontext, selectedmovie.getmId());
                            ProviderUtils.deleteMovieTrailers(mcontext,selectedmovie.getmId());
                            testFavBut = "UNFavorite it !";
                        }
                    }
                });
        materialFavoriteButtonNice.setOnFavoriteAnimationEndListener(
                new MaterialFavoriteButton.OnFavoriteAnimationEndListener() {
                    @Override
                    public void onAnimationEnd(MaterialFavoriteButton buttonView, boolean favorite) {
                        if (warnings != null)
                            warnings.cancel();

                        warnings = Toast.makeText(mcontext, testFavBut, Toast.LENGTH_SHORT);
                        warnings.show();
                    }
                });

        mtitle = (TextView) findViewById(R.id.movie_title);
        mCardTitle = (TextView) findViewById(R.id.movie_card_title_id);
        mreleaseDate = (TextView) findViewById(R.id.movie_card_date_id);
        mRate = (TextView) findViewById(R.id.movie_rate);
        mCardRate = (TextView) findViewById(R.id.movie_card_rate_id);
        mOverview = (TextView) findViewById(R.id.movie_overview);
        mBackdrop = (ImageView) findViewById(R.id.Backdrop) ;
        mPoster = (ImageView) findViewById(R.id.movie_card_Image_Id) ;
        mCardVotesC = (TextView) findViewById(R.id.movie_card_ratevotes_id) ;
        mCardAdult = (TextView) findViewById(R.id.movie_card_Adult_id) ;
        trailersrecyclerView = (RecyclerView) findViewById(R.id.Trailers_recycler_view);
        reviewsrecyclerview = (RecyclerView) findViewById(R.id.Reviews_recycler_view);


        if (selectedmovie.ismAdult()){
            mStrAdult = "yes";
        }


        mtitle.setText(selectedmovie.getmTitle());
        mCardTitle.setText(selectedmovie.getmTitle());
        mreleaseDate.setText("Release Date: "+selectedmovie.getmReleaseDate());
        rate = selectedmovie.getmVote();
        mRate.setText(String.valueOf(rate));
        mCardRate.setText("Rate: "+String.valueOf(rate)+" /10");
        mOverview.setText("Overview: "+selectedmovie.getmOverView());
        mCardVotesC.setText("Votes Count: "+selectedmovie.getmVoteCount());
        mCardAdult.setText("Adult Status: "+mStrAdult);

        Log.i("backDrop link",this.getString(R.string.poster_url)+selectedmovie.getmBackdropPath());
        Picasso.with(this)
                .load(this.getString(R.string.poster_url)+selectedmovie.getmBackdropPath())
                .error(ContextCompat.getDrawable(this, R.drawable.content))
                .into(mBackdrop);


        Picasso.with(this)
                .load(this.getString(R.string.poster_url)+selectedmovie.getmPosterPath())
                .error(ContextCompat.getDrawable(this, R.drawable.content))
                .into(mPoster);


        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(TRAILERS_LOADER_ID, null, MovieTrailerCallbacks);
            loaderManager.initLoader(REVIEWS_LOADER_ID, null, MovieReviewCallbacks);

        } else {


        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        trailersrecyclerView.setLayoutManager(layoutManager);
        trailersrecyclerView.setHasFixedSize(true);
        trailersrecyclerView.setItemAnimator(new DefaultItemAnimator());
        trailersrecyclerView.setAdapter(mTrailersAdapter);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);

        reviewsrecyclerview.setLayoutManager(layoutManager2);
        reviewsrecyclerview.setHasFixedSize(true);
        reviewsrecyclerview.setItemAnimator(new DefaultItemAnimator());
        reviewsrecyclerview.setAdapter(mReviewAdapter);

        mTrailersAdapter.notifyDataSetChanged();
        mReviewAdapter.notifyDataSetChanged();




    }



    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle("Movie Details");
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }



    @Override
    public void onListitemClick(int clickitemIndex) {

        MovieTrailer currentMovieTrailer = mTrailers.get(clickitemIndex);
        String trailerKey = currentMovieTrailer.getmKey();
        String temp = mcontext.getString(R.string.trailer_base_url)+"?";
        Uri baseUri = Uri.parse(temp);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter(mcontext.getString(R.string.trailer_param_key), trailerKey);
        Log.i("youtube Urllll",uriBuilder.toString());

        Intent trailerIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uriBuilder.toString()));
        if (trailerIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(trailerIntent);
        } else {
            if (warnings != null)
                warnings.cancel();

            warnings = Toast.makeText(mcontext, "Sorry, Install YouTube Or Any browser.", Toast.LENGTH_SHORT);
            warnings.show();
        }




    }
}
