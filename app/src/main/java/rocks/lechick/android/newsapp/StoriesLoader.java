package rocks.lechick.android.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by alek on 21/05/2018.
 */

public class StoriesLoader extends AsyncTaskLoader<List<Story>>{

        private static final String LOG_TAG = StoriesLoader.class.getName();

        /** Query URL */
        private String mUrl;

        /**
         * Constructs a new {@link StoriesLoader}.
         *
         * @param context of the activity
         * @param url to load data from
         */
        public StoriesLoader(Context context, String url) {
            super(context);
            mUrl = url;
        }

        @Override
        protected void onStartLoading() {
            forceLoad();
        }

        /**
         * This is on a background thread.
         */
        @Override
        public List<Story> loadInBackground() {
            if (mUrl == null) {
                return null;
            }

            // Perform the network request, parse the response, and extract a list of earthquakes.
            List<Story> stories = StoriesQueryUtils.fetchStoryData(mUrl);
            return stories;
        }
    }
