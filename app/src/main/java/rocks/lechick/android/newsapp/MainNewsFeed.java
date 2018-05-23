package rocks.lechick.android.newsapp;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Loader;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainNewsFeed extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Story>>{

    private static final String LOG_TAG = MainNewsFeed.class.getName();
    private static final String GUARDIAN_REQUEST_URL = "https://content.guardianapis.com/artanddesign?show-blocks=body&show-tags=contributor&api-key=2b89f3b3-84d7-4af3-bbbd-d71d7b47cb66";
    private static final int STORY_LOADER_ID = 1;
    private StoryAdaptor mAdapter;
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView storiesListView = (ListView) findViewById(R.id.list);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        storiesListView.setEmptyView(mEmptyStateTextView);

        mAdapter = new StoryAdaptor(this, new ArrayList<Story>());

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
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public Loader<List<Story>> onCreateLoader(int i, Bundle Bundle){
        return new StoriesLoader(this, GUARDIAN_REQUEST_URL);
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

}
