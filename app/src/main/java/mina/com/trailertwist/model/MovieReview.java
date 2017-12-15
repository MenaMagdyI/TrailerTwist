package mina.com.trailertwist.model;

/**
 * Created by Mena on 12/9/2017.
 */

public class MovieReview {

    private String mId;
    private String mAuthor;
    private String mContent;
    private String mUrl;

    public MovieReview(String mId, String mAuthor, String mContent, String mUrl) {
        this.mId = mId;
        this.mAuthor = mAuthor;
        this.mContent = mContent;
        this.mUrl = mUrl;
    }

    public MovieReview(String mAuthor, String mContent) {
        this.mAuthor = mAuthor;
        this.mContent = mContent;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public void setmAuthor(String mAuthor) {
        this.mAuthor = mAuthor;
    }

    public String getmContent() {
        return mContent;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }
}
