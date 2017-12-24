package mina.com.trailertwist.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import mina.com.trailertwist.R;
import mina.com.trailertwist.model.MovieReview;

/**
 * Created by Mena on 12/9/2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.TrailerViewHolder> {

    private Context mContext;
    private List<MovieReview> ReviewsList;

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_item, parent, false);
        return new TrailerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        MovieReview review = ReviewsList.get(position);

        holder.mrUser.setText(review.getmAuthor());
        holder.mrContent.setText(review.getmContent());

    }

    @Override
    public int getItemCount() {
        return ReviewsList.size();
    }


    public class TrailerViewHolder extends RecyclerView.ViewHolder {

        public TextView mrUser, mrContent;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            mrUser = (TextView) itemView.findViewById(R.id.review_user);
            mrContent = (TextView) itemView.findViewById(R.id.review_content);

        }


    }

    public ReviewAdapter(Context mContext, List<MovieReview> reviewsList) {
        this.mContext = mContext;
        ReviewsList = reviewsList;
    }
}
