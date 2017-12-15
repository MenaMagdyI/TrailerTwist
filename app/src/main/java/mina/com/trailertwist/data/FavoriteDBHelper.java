package mina.com.trailertwist.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import mina.com.trailertwist.data.FavoriteContract.*;

/**
 * Created by Mena on 12/14/2017.
 */

public class FavoriteDBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "favorites.db";
    public static final int DB_VERSION = 1;

    public FavoriteDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_CREATE_MOVIES_TABLE =  "CREATE TABLE " + MovieEntry.TABLE_NAME + " ("
                + MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MovieEntry.MOVIE_TITLE + " TEXT, "
                + MovieEntry.RELEASE_DATE + " TEXT, "
                + MovieEntry.PLOT + " TEXT, "
                + MovieEntry.RATE + " TEXT, "
                + MovieEntry.POSTER + " TEXT);";


        final String SQL_CREATE_TRAILERS_TABLE =
                "CREATE TABLE " + FavoriteContract.TrailerEntry.TABLE_NAME + " ( " +
                        FavoriteContract.TrailerEntry.MOVIE_ID      + " INTEGER, " +
                        FavoriteContract.TrailerEntry.TITLE         + " TEXT, " +
                        FavoriteContract.TrailerEntry.YOUTUBE_KEY   + " TEXT, " +
                        "FOREIGN KEY(" + FavoriteContract.TrailerEntry.MOVIE_ID + ") " +
                        "REFERENCES " + MovieEntry.TABLE_NAME + "(" + MovieEntry._ID +") " + " )";



        final String SQL_CREATE_REVIEWS_TABLE =
                "CREATE TABLE " + FavoriteContract.ReviewEntry.TABLE_NAME + " ( " +
                        FavoriteContract.ReviewEntry.MOVIE_ID  + " INTEGER, " +
                        FavoriteContract.ReviewEntry.AUTHOR    + " TEXT, " +
                        FavoriteContract.ReviewEntry.CONTENT   + " TEXT, " +
                        "FOREIGN KEY(" + FavoriteContract.ReviewEntry.MOVIE_ID + ") " +
                        "REFERENCES " + MovieEntry.TABLE_NAME + "(" + MovieEntry._ID +") " + " )";



        db.execSQL(SQL_CREATE_MOVIES_TABLE);
        db.execSQL(SQL_CREATE_TRAILERS_TABLE);
        db.execSQL(SQL_CREATE_REVIEWS_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        final String SQL_UPGRADE_VERSION_QUERY = "DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME;
        sqLiteDatabase.execSQL(SQL_UPGRADE_VERSION_QUERY);

        onCreate(sqLiteDatabase);

    }
}
