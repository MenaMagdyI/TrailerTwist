package mina.com.trailertwist;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import mina.com.trailertwist.model.Movie;

public class MovieDetailsActivity extends AppCompatActivity {

    private Movie selectedmovie;
    private TextView mtitle, mreleaseDate,mRate,mOverview;
    private ImageView mPoster;
    private double rate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        selectedmovie = getIntent().getParcelableExtra(MainActivity.MOVIE_INFO);
        mtitle = (TextView) findViewById(R.id.movie_title);
        mreleaseDate = (TextView) findViewById(R.id.movie_release_date);
        mRate = (TextView) findViewById(R.id.movie_rate);
        mOverview = (TextView) findViewById(R.id.movie_overview);
        mPoster = (ImageView) findViewById(R.id.movie_poster) ;

        mtitle.setText(selectedmovie.getmTitle());
        mreleaseDate.setText(selectedmovie.getmReleaseDate());
        rate = selectedmovie.getmVote();
        mRate.setText(String.valueOf(rate));
        mOverview.setText(selectedmovie.getmOverView());

        Picasso.with(this)
                .load(this.getString(R.string.poster_url)+selectedmovie.getmPosterPath())
                .error(ContextCompat.getDrawable(this, R.drawable.content))
                .into(mPoster);
    }
}
