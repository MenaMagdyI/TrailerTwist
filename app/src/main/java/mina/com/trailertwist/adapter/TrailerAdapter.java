package mina.com.trailertwist.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import mina.com.trailertwist.R;
import mina.com.trailertwist.model.MovieTrailer;

/**
 * Created by Mena on 12/9/2017.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    private Context mContext;
    private List<MovieTrailer> TrailerList;

    private final ListItemClickListener mOnclickListner;

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailer_item, parent, false);

        return new TrailerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {

        MovieTrailer mTrailer = TrailerList.get(position);

        holder.mtTitle.setText(mTrailer.getmName());
        holder.mtType.setText(mTrailer.getmType());

    }

    @Override
    public int getItemCount() {
        return TrailerList.size();
    }


    public interface ListItemClickListener{
        void onListitemClick(int clickitemIndex);
    }


    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView mtTitle, mtType;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            mtTitle = (TextView) itemView.findViewById(R.id.trailer_title);
            mtType = (TextView) itemView.findViewById(R.id.trailer_type);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mOnclickListner.onListitemClick(position);

        }
    }

    public TrailerAdapter(Context mContext, List<MovieTrailer> TrailerList, ListItemClickListener mOnclickListner ){
        this.mContext = mContext;
        this.TrailerList = TrailerList;
        this.mOnclickListner = mOnclickListner;
    }



}
