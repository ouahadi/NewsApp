package rocks.lechick.android.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsFeedActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Story>>{

    private static final String LOG_TAG = NewsFeedActivity.class.getName();
    private static final String GUARDIAN_REQUEST_URL = "https://content.guardianapis.com/";
    private static final int STORY_LOADER_ID = 1;
    private StoryAdapter mAdapter;
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView storiesListView = (ListView) findViewById(R.id.list);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        storiesListView.setEmptyView(mEmptyStateTextView);

        mAdapter = new StoryAdapter(this, new ArrayList<Story>());

        storiesListView.setAdapter(mAdapter);

        storiesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Story currentStory = mAdapter.getItem(position);

                Uri storyUrl = Uri.parse(currentStory.getmUrl());

                Intent intent = new Intent(Intent.ACTION_VIEW, storyUrl);
                startActivity(intent);
            }
        });

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(STORY_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.empty_progress_bar);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_stories);
        }
    }

    @Override
    public Loader<List<Story>> onCreateLoader(int i, Bundle Bundle){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String newsCategoryNumber = sharedPrefs.getString("selected_news_category", "0");
        int categoryNumber = Integer.parseInt(newsCategoryNumber);
        String newsCategory = getResources().getStringArray(R.array.storyCategoriesArray)[categoryNumber];

        Uri baseUri = Uri.parse(GUARDIAN_REQUEST_URL);

        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendPath(newsCategory.toLowerCase());
        uriBuilder.appendQueryParameter("show-blocks", "body");
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("api-key", "2b89f3b3-84d7-4af3-bbbd-d71d7b47cb66");

        Log.v("The uri I've built", uriBuilder.toString());

        return new StoriesLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Story>> loader, List<Story> stories){
        View loadingIndicator = findViewById(R.id.empty_progress_bar);
        loadingIndicator.setVisibility(View.GONE);
        // Update empty state with no connection error message
        mEmptyStateTextView.setText(R.string.no_internet_connection);

        if (stories != null && !stories.isEmpty()) {
            mAdapter.addAll(stories);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Story>> loader){
        mAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.stories_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
