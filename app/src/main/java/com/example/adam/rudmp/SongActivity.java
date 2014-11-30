package com.example.adam.rudmp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.adam.rudmp.model.Song;

/**
 * Created by adam on 30/11/14.
 */
public class SongActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);

        // get the passed song from the bundle
        Song s = (Song) getIntent().getParcelableExtra("selectedSong");

        TextView songInfo = (TextView) findViewById(R.id.songInfo);
        songInfo.setText(s.showDetail());
    }
}
