package rocks.lechick.android.newsapp;

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

/**
 * Created by alek on 21/05/2018.
 */

public final class StoriesQueryUtils {


    public static final String LOG_TAG = StoriesQueryUtils.class.getSimpleName();


    private StoriesQueryUtils() {
    }

    public static List<Story> fetchStoryData(String requestURL) {
        //create URL object
        URL url = createUrl(requestURL);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        List<Story> stories = extractFeatureFromJson(jsonResponse);

        return stories;


    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
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
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
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

    private static List<Story> extractFeatureFromJson(String storiesJSON){

        if(TextUtils.isEmpty(storiesJSON)){
            return null;
        }

        //create ArrayList to start adding stories
        List<Story> stories = new ArrayList<>();

        try{
            JSONObject baseJsonResponse = new JSONObject(storiesJSON);
            JSONObject response = baseJsonResponse.getJSONObject("response");

            JSONArray storiesArray = response.getJSONArray("results");

                    for (int i = 0; i < storiesArray.length(); i++){
                JSONObject currentStory = storiesArray.getJSONObject(i);

                String title = currentStory.getString("webTitle");
                String section = currentStory.getString("sectionName");
                String date = currentStory.getString("webPublicationDate");
                String link = currentStory.getString("webUrl");
                String author = " ";

                JSONArray tagsArray = currentStory.getJSONArray("tags");
                if (tagsArray != null) {
                    for (int ii = 0; ii < tagsArray.length(); ii++) {

                        JSONObject authorJson = tagsArray.getJSONObject(ii);
                        author = authorJson.getString("webTitle");
                    }
                }
                        JSONObject blocks = currentStory.getJSONObject("blocks");
                        JSONArray body = blocks.getJSONArray("body");
                        JSONObject firstInfo = body.getJSONObject(0);
                        String summary = firstInfo.getString("bodyTextSummary");

            Story story = new Story(title, section, author, date, summary, link);

            stories.add(story);
        }

    } catch (JSONException e) {
        // If an error is thrown when executing any of the above statements in the "try" block,
        // catch the exception here, so the app doesn't crash. Print a log message
        // with the message from the exception.
        Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
    }

    // Return the list of earthquakes
        return stories;
    }
}