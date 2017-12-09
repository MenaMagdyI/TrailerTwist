package mina.com.trailertwist;



import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import mina.com.trailertwist.model.Movie;

public class MovieDetailsActivity extends AppCompatActivity {

    private Movie selectedmovie;
    private TextView mtitle, mCardTitle, mreleaseDate,mRate,mOverview,mCardRate,mCardVotesC,mCardAdult;
    private ImageView mPoster, mBackdrop;
    private double rate;
    private String mStrAdult = "no";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initCollapsingToolbar();


        selectedmovie = getIntent().getParcelableExtra(MainActivity.MOVIE_INFO);


        if (selectedmovie.ismAdult()){
            mStrAdult = "yes";
        }

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
}
