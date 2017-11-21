package mina.com.trailertwist.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by Mena on 11/17/2017.
 */

public class Movie implements Parcelable {

    private int mVoteCount;
    private int mId;
    private double mVote;
    private String mTitle;
    private String mPosterPath;
    private String mBackdropPath;
    private boolean mAdult;
    private String mOverView;
    private String mReleaseDate;

    public Movie(double mVote, String mTitle, String mPosterPath) {
        this.mVote = mVote;
        this.mTitle = mTitle;
        this.mPosterPath = mPosterPath;
        //Log.i("constructore:",mTitle+" created !");
    }

    public Movie(int mVoteCount, int mId, double mVote, String mTitle, String mPosterPath, String mBackdropPath, boolean mAdult, String mOverView, String mReleaseDate) {
        this.mVoteCount = mVoteCount;
        this.mId = mId;
        this.mVote = mVote;
        this.mTitle = mTitle;
        this.mPosterPath = mPosterPath;
        this.mBackdropPath = mBackdropPath;
        this.mAdult = mAdult;
        this.mOverView = mOverView;
        this.mReleaseDate = mReleaseDate;
    }

    protected Movie(Parcel in) {
        mVoteCount = in.readInt();
        mId = in.readInt();
        mVote = in.readDouble();
        mTitle = in.readString();
        mPosterPath = in.readString();
        mBackdropPath = in.readString();
        mAdult = in.readByte() != 0;
        mOverView = in.readString();
        mReleaseDate = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public int getmVoteCount() {
        return mVoteCount;
    }

    public void setmVoteCount(int mVoteCount) {
        this.mVoteCount = mVoteCount;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public double getmVote() {
        return mVote;
    }

    public void setmVote(double mVote) {
        this.mVote = mVote;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmPosterPath() {
        return mPosterPath;
    }

    public void setmPosterPath(String mPosterPath) {
        this.mPosterPath = mPosterPath;
    }

    public String getmBackdropPath() {
        return mBackdropPath;
    }

    public void setmBackdropPath(String mBackdropPath) {
        this.mBackdropPath = mBackdropPath;
    }

    public boolean ismAdult() {
        return mAdult;
    }

    public void setmAdult(boolean mAdult) {
        this.mAdult = mAdult;
    }

    public String getmOverView() {
        return mOverView;
    }

    public void setmOverView(String mOverView) {
        this.mOverView = mOverView;
    }

    public String getmReleaseDate() {
        return mReleaseDate;
    }

    public void setmReleaseDate(String mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mVoteCount);
        parcel.writeInt(mId);
        parcel.writeDouble(mVote);
        parcel.writeString(mTitle);
        parcel.writeString(mPosterPath);
        parcel.writeString(mBackdropPath);
        parcel.writeByte((byte) (mAdult ? 1 : 0));
        parcel.writeString(mOverView);
        parcel.writeString(mReleaseDate);
    }
}
