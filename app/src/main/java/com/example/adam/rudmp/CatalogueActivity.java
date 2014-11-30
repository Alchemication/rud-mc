package com.example.adam.rudmp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.example.adam.rudmp.controller.CatalogueController;
import com.example.adam.rudmp.model.Song;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

public class CatalogueActivity extends Activity {

    private static final String MY_PREFS_NAME = "RUD_MP";

    private static final String SAVED_BUNDLE_SONGS_KEY = "currentSongs";

    private static final String PREFERENCES_GREETINGS_KEY = "greeting";

    private CatalogueController catalogueController;

    private ListView songListView;

    private TextView errorMsgTextView;

    private ArrayAdapter<Song> adapter;

    private ArrayList<Song> songList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogue);

        // find GUI components
        this.errorMsgTextView = (TextView) findViewById(R.id.errorMessage);
        this.songListView     = (ListView) findViewById(R.id.songList);

        // create shorthand for controller
        this.catalogueController = CatalogueController.getInstance();

        // get songs from the bundle if available,
        // if not - load from the resource
        if (savedInstanceState == null || !savedInstanceState.containsKey(SAVED_BUNDLE_SONGS_KEY)) {

            // set songs from the resource file using controller method
            InputStream stream    = getResources().openRawResource(R.raw.songs);
            String retrieveStatus = this.catalogueController.createListFromStream(stream);

            // make sure file retrieval was successful,
            // if not - set error message
            if (!retrieveStatus.equals(CatalogueController.OK_STATUS)) {
                this.setErrorMessage(retrieveStatus);
                return;
            }

            // show toast with the last app access
            this.manageGreetings();

        } else {
            // we can read from the bundle now
            ArrayList<Song> savedSongList = savedInstanceState.getParcelableArrayList(SAVED_BUNDLE_SONGS_KEY);
            this.catalogueController.setSongList(savedSongList);
        }

        // get list of songs from controller
        this.songList = this.catalogueController.getSongList();

        // create adapter for the list view
        this.adapter = new ArrayAdapter<Song>(
            this,
            android.R.layout.simple_list_item_1,
            android.R.id.text1,
            this.songList
        );

        // update list view with the songs
        this.songListView.setAdapter(adapter);

        // attach click listener on the listView element,
        // this will launch a new activity, which will display the song detail
        this.songListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView parent, View v, int position, long id)
            {

            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {

        // save current list into the bundle,
        // in case items are shuffled and users are
        // flipping their devices around
        savedInstanceState.putParcelableArrayList(SAVED_BUNDLE_SONGS_KEY, this.songList);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.catalogue, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;

            case R.id.actionShuffle:
                this.shuffleSongs();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Hide listView and show textView with the error message
     *
     * @param errorMessage Error to be displayed
     */
    private void setErrorMessage(String errorMessage) {
        songListView.setVisibility(View.GONE);
        errorMsgTextView.setVisibility(View.VISIBLE);
        errorMsgTextView.setText(errorMessage);
        //Log.e("error", errorMessage);
    }

    /**
     * Get controller to shuffle songs and update
     * listView by notifying it of a change
     */
    private void shuffleSongs() {
        this.catalogueController.shuffleSongs();
        this.songList = this.catalogueController.getSongList();
        this.adapter.notifyDataSetChanged();
    }

    /**
     * Retrieve last app startup from SharedPreferences
     * and show it in the Toast
     */
    private void manageGreetings() {

        // obtain preferences from activity
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        // set toast values
        String greeting         = prefs.getString(PREFERENCES_GREETINGS_KEY, "Hello first time!"); // provide default
        Context context         = getApplicationContext();
        int duration            = Toast.LENGTH_SHORT;

        // set and display the toast
        Toast toast = Toast.makeText(context, greeting, duration);
        toast.show();

        // obtain preferences editor and
        // save new greeting in preferences for the next time
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        String appStartup               = DateFormat.getDateTimeInstance().format(new Date());
        editor.putString(PREFERENCES_GREETINGS_KEY, "Last startup: " + appStartup);
        editor.apply();
    }
}
