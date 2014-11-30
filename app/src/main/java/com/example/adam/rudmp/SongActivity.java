package com.example.adam.rudmp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adam.rudmp.model.Song;

/**
 * Created by adam on 30/11/14.
 */
public class SongActivity extends Activity implements View.OnClickListener {

    private Song selectedSong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);

        // get the passed song from the bundle,
        // store current song for further reference
        this.selectedSong = getIntent().getParcelableExtra("selectedSong");

        // update text in UI
        TextView songInfo = (TextView) findViewById(R.id.songInfo);
        songInfo.setText(this.selectedSong.showDetail());

        // set up listeners
        setupListeners();
    }

    /**
     * Set up button listeners
     */
    private void setupListeners() {

        Button btnView = (Button) findViewById(R.id.viewBtn);
        btnView.setOnClickListener(this);

        Button btnShare = (Button) findViewById(R.id.shareBtn);
        btnShare.setOnClickListener(this);
    }

    /**
     * Use one click listener for all the buttons
     *
     * @param v clicked object
     */
    public void onClick(View v) {

        // define the button that invoked the listener by id
        switch (v.getId()) {

            // open dialog box and follow the link
            // using implicit intent
            case R.id.viewBtn:

                launchWebPageIntent(this.selectedSong.getLink());
                break;

            // share the link using implicit intent
            case R.id.shareBtn:

                shareUrl(this.selectedSong.getLink(), this.selectedSong.getTitle());
                break;
        }
    }

    /**
     * Share link using capable apps
     *
     * @param url url to share
     * @param title title to display
     */
    private void shareUrl(String url, String title) {

        final String urlToOpen      = url;
        final String titleToDisplay = title;

        new AlertDialog.Builder(this)
                .setTitle("Are you sure")
                .setMessage("Do you really want to share the link?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        Intent share = new Intent(android.content.Intent.ACTION_SEND);
                        share.setType("text/plain");
                        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

                        // Add data to the intent, the receiving app will decide
                        // what to do with it.
                        share.putExtra(Intent.EXTRA_SUBJECT, titleToDisplay);
                        share.putExtra(Intent.EXTRA_TEXT, urlToOpen);

                        startActivity(Intent.createChooser(share, "Share link!"));
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    /**
     * Launch apps capable of opening html,
     * and send the link of the song
     */
    private void launchWebPageIntent(String url) {

        final String urlToOpen = url;

        new AlertDialog.Builder(this)
                .setTitle("Are you sure")
                .setMessage("Do you really want to follow the link?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // create new intent
                        Intent launchWebPageIntent = new Intent(Intent.ACTION_VIEW);

                        // put the link in the bundle,
                        // method putExtra() from the notes
                        // was triggering an Exception, used following resource:
                        // @see http://stackoverflow.com/questions/3004515/android-sending-an-intent-to-browser-to-open-specific-url
                        launchWebPageIntent.setData(Uri.parse(urlToOpen));

                        // kick off intent
                        startActivity(launchWebPageIntent);
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }
}
