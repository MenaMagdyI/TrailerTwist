package mina.com.trailertwist.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import mina.com.trailertwist.R;
import mina.com.trailertwist.model.Movie;
import mina.com.trailertwist.model.MovieReview;
import mina.com.trailertwist.model.MovieTrailer;

/**
 * Created by Mena on 11/19/2017.
 */

public class NetworkQueryUtils {

    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = NetworkQueryUtils.class.getSimpleName();


    private NetworkQueryUtils() {
    }


    /**
     * to create the url
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }


    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the Movie JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }


    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<Movie> extractFeatureFromJson(String movieJSON, Context context) {

        List<Movie> movies = new ArrayList<>();
        if (TextUtils.isEmpty(movieJSON) || movieJSON.length() == 0) {
            return movies;
        }


        try {

            int mVoteCount;
            int mId;
            double mVote;
            String mTitle;
            String mPosterPath;
            String mBackdropPath;
            boolean mAdult;
            String mOverView;
            String mreleaseDate;
            // Log.i("json test","before jsonresponse");
            JSONObject baseJsonResponse = new JSONObject(movieJSON);
            // Log.i("json test","after jsonresponse");
            JSONArray results = baseJsonResponse.getJSONArray(context.getString(R.string.json_key_results_array));
            // Log.i("json test","after results");
            // For each movie in the moviesArray
            for (int i = 0; i < results.length(); i++) {


                JSONObject movie = results.getJSONObject(i);
                mId = movie.getInt(context.getString(R.string.json_key_movie_id));
                mTitle = movie.getString(context.getString(R.string.json_key_movie_title));
                mVoteCount = movie.getInt(context.getString(R.string.json_key_movie_rate_count));
                mreleaseDate = movie.getString(context.getString(R.string.json_key_release_date));
                mPosterPath = movie.getString(context.getString(R.string.json_key_poster_path));
                mBackdropPath = movie.getString(context.getString(R.string.json_key_Backdrop_path));
                mOverView = movie.getString(context.getString(R.string.json_key_movie_plot));
                mAdult = movie.getBoolean(context.getString(R.string.json_key_movie_adult));
                mVote = movie.getDouble(context.getString(R.string.json_key_movie_rate));

                // Log.i("name: " , mTitle);
                movies.add(new Movie(mVoteCount, mId, mVote, mTitle, mPosterPath, mBackdropPath, mAdult, mOverView, mreleaseDate));
            }

        } catch (JSONException e) {

            Log.e("NetworkQueryUtils", "Problem parsing the Movie JSON results", e);
        }


        return movies;
    }

    private static List<MovieTrailer> extractTrailersFromJson(String jsonResponse, Context context) {
        ArrayList<MovieTrailer> trailers = new ArrayList<>();
        if (jsonResponse == null || jsonResponse.length() == 0) {
            return trailers;
        }

        try {
            JSONObject root = new JSONObject(jsonResponse);
            JSONArray results = root.getJSONArray(context.getString(R.string.json_key_results_array));

            String trailerId;
            String trailerKey;
            String trailerSite;
            String trailerTitle;
            String trailerSize;
            String trailerType;
            for (int i = 0; i < results.length(); i++) {
                JSONObject trailer = results.getJSONObject(i);
                trailerId = trailer.getString(context.getString(R.string.json_key_trailer_Id));
                trailerTitle = trailer.getString(context.getString(R.string.json_key_trailer_title));
                trailerKey = trailer.getString(context.getString(R.string.json_key_trailer_key));
                trailerSite = trailer.getString(context.getString(R.string.json_key_trailer_site));
                trailerSize = trailer.getString(context.getString(R.string.json_key_trailer_size));
                trailerType = trailer.getString(context.getString(R.string.json_key_trailer_type));
                trailers.add(new MovieTrailer(trailerId, trailerKey, trailerTitle, trailerSite, trailerSize, trailerType));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return trailers;
    }


    private static List<MovieReview> extractReviewsFromJson(String jsonResponse, Context context) {
        ArrayList<MovieReview> reviews = new ArrayList<>();
        if (jsonResponse == null || jsonResponse.length() == 0) {
            return reviews;
        }

        try {
            JSONObject root = new JSONObject(jsonResponse);
            JSONArray results = root.getJSONArray(context.getString(R.string.json_key_results_array));

            String id;
            String author;
            String content;
            String url;

            for (int i = 0; i < results.length(); i++) {
                JSONObject review = results.getJSONObject(i);
                id = review.getString(context.getString(R.string.json_key_review_Id));
                author = review.getString(context.getString(R.string.json_key_review_author));
                content = review.getString(context.getString(R.string.json_key_review_content));
                url = review.getString(context.getString(R.string.json_key_review_url));

                reviews.add(new MovieReview(id, author, content, url));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return reviews;
    }


    public static List<Movie> fetchDatafMovies(String requestUrl, Context context) {

        URL url = createUrl(requestUrl);

        // Log.i("fetch 11111: ",url.toString());
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(NetworkQueryUtils.class.getSimpleName(), "Problem making the HTTP request.", e);
        }

        // Log.i("extractFeature : ","2222222222");

        List<Movie> movies = extractFeatureFromJson(jsonResponse, context);
        // Log.i("afterFeatureFromJson : ",movies.get(0).getmTitle());
        return movies;
    }


    public static List<MovieTrailer> fetchDatafTrailers(String requestUrl, Context context) {

        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(NetworkQueryUtils.class.getSimpleName(), "Problem making the HTTP request.", e);
        }
        List<MovieTrailer> Trailers = extractTrailersFromJson(jsonResponse, context);

        return Trailers;
    }


    public static List<MovieReview> fetchDatafReviews(String requestUrl, Context context) {

        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(NetworkQueryUtils.class.getSimpleName(), "Problem making the HTTP request.", e);
        }
        List<MovieReview> Reviews = extractReviewsFromJson(jsonResponse, context);

        return Reviews;
    }


}
