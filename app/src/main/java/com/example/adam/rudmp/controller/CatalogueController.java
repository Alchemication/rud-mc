package com.example.adam.rudmp.controller;

import android.os.Environment;
import android.util.Log;

import com.example.adam.rudmp.model.Song;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by adam on 29/11/14.
 */
public class CatalogueController {

    // this is a row sections delimiter in the file
    private static final String FILE_DELIMITER = "##";

    // this will be returned on successful file retrieval
    public static final String OK_STATUS = "ok";

    private static CatalogueController instance;

    private ArrayList<Song> songList;

    private CatalogueController() {
        this.songList = new ArrayList<Song>();
    }

    public static CatalogueController getInstance() {

        if (instance == null) {
            instance = new CatalogueController();
        }

        return instance;
    }

    public ArrayList<Song> getSongList() {
        return songList;
    }

    public void setSongList(ArrayList<Song> songList) {
        this.songList = songList;
    }

    public void addSong(Song song) {
        this.songList.add(song);
    }

    /**
     * Parse file and add songs from the file to the songList
     *
     * @param stream Content of the file
     * @return return update status
     */
    public String createListFromStream(InputStream stream) {
        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

            String line;
            while ((line = reader.readLine()) != null) {

                String[] songDetails = line.split(FILE_DELIMITER);
                Song song            = new Song(songDetails[0], songDetails[1], songDetails[2]);

                this.addSong(song);
            }

            reader.close();
        } catch (IOException ex) {
            return "File Error Occurred: " + ex.getMessage();
        }

        return OK_STATUS;
    }

    public void shuffleSongs() {
        Collections.shuffle(songList);
    }

    public void clearSongList() {
        this.songList.clear();
    }
}
