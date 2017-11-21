package mina.com.trailertwist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import mina.com.trailertwist.model.Movie;

public class MovieDetailsActivity extends AppCompatActivity {

    private Movie selectedmovie;
    private TextView mtitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        selectedmovie = getIntent().getParcelableExtra(MainActivity.MOVIE_INFO);
        mtitle = (TextView) findViewById(R.id.movie_title);
        mtitle.setText(selectedmovie.getmTitle());
    }
}
