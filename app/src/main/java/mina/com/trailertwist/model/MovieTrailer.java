package mina.com.trailertwist.model;

/**
 * Created by Mena on 12/9/2017.
 */

public class MovieTrailer {

    private String mId;
    private String mKey;
    private String mName;
    private String mSite;
    private String mSize;
    private String mType;

    public MovieTrailer(String mId, String mKey, String mName, String mSite, String mSize, String mType) {
        this.mId = mId;
        this.mKey = mKey;
        this.mName = mName;
        this.mSite = mSite;
        this.mSize = mSize;
        this.mType = mType;
    }

    public MovieTrailer(String mKey, String mName) {
        this.mKey = mKey;
        this.mName = mName;
    }

    public String getmKey() {
        return mKey;
    }

    public void setmKey(String mKey) {
        this.mKey = mKey;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmSite() {
        return mSite;
    }

    public void setmSite(String mSite) {
        this.mSite = mSite;
    }

    public String getmSize() {
        return mSize;
    }

    public void setmSize(String mSize) {
        this.mSize = mSize;
    }

    public String getmType() {
        return mType;
    }

    public void setmType(String mType) {
        this.mType = mType;
    }
}
