package mina.com.trailertwist.model;

import android.util.Log;

/**
 * Created by Mena on 11/17/2017.
 */

public class Movie {

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





}
