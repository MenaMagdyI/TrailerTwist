package mina.com.trailertwist.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import mina.com.trailertwist.R;
import mina.com.trailertwist.model.Movie;

/**
 * Created by Mena on 11/17/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {

    private Context mContext;
    private List<Movie> movieList;

    private int lastPosition = -1;

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView title, rate;
        public ImageView thumbnail, smallmenu;
        public View loadingIndicator;

        public MyViewHolder(View itemView) {
            super(itemView);

            //Log.i("Movie adapter: ","in the MyViwHolder method");
            title = (TextView) itemView.findViewById(R.id.title);
            rate = (TextView) itemView.findViewById(R.id.rate);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail_image);
            smallmenu = (ImageView) itemView.findViewById(R.id.smallmenu);
            loadingIndicator = itemView.findViewById(R.id.posterprogress);
            loadingIndicator.setVisibility(View.VISIBLE);


        }
    }

    public MovieAdapter(Context mContext, List<Movie> movieList) {
        this.mContext = mContext;
        this.movieList = movieList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_cardview, parent, false);

        //Log.i("Movie adapter: ","in onCreateViewHolder method");

        return new MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {



        Movie movie = movieList.get(position);

        holder.title.setText(movie.getmTitle());
        holder.rate.setText("Rate: "+movie.getmVote());

        Picasso.with(mContext)
                .load(mContext.getString(R.string.poster_url)+movie.getmPosterPath())
                .error(ContextCompat.getDrawable(mContext, R.drawable.content))
                .into(holder.thumbnail);

        // to hide the loading indicator after loading image is completed
       /* View loadingIndicator = null;
        loadingIndicator = loadingIndicator.findViewById(R.id.posterprogress);*/
        holder.loadingIndicator.setVisibility(View.GONE);
        holder.thumbnail.setVisibility(View.VISIBLE);



        //Log.i("Movie adapter: ","in onBindViewHolder method after picasso");
        holder.smallmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.smallmenu);
            }
        });


    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    /**
     * Showing popup smallmoviemenu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
        // inflate smallmoviemenu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.smallmoviemenu, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    /**
     * Click listener for popup smallmoviemenu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:
                    Toast.makeText(mContext, "test favourite for stage 2", Toast.LENGTH_SHORT).show();
                    return true;
            }
            return false;
        }
    }

    /*

    will add animation to recycler view later here.
    search about it - animate left and right.

     */


}
