package edu.cvtc.android.capstonemusic;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nicholas on 11/22/17.
 */

public class SongListActivity extends ListActivity {
    private ListView songList;
    private ArrayList<Music> allMusic;
    private AppDatabase database;
    MusicAdapter listAdapter;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.song_list);
        songList = (ListView) findViewById(R.id.songList);

        database = AppDatabase.getDatabase(getApplicationContext());
        allMusic = (ArrayList<Music>) database.musicDAO().getAllMusic();

        listAdapter = new MusicAdapter(this, R.layout.music_item, allMusic);
        setListAdapter(listAdapter);





}
}
