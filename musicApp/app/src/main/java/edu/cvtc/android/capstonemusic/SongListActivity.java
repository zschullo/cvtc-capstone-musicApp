package edu.cvtc.android.capstonemusic;

import android.app.ListActivity;
import android.content.Intent;
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
    private ArrayList<Music> allMusic;
    private AppDatabase database;
    MusicAdapter listAdapter;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.song_list);
        database = AppDatabase.getDatabase(getApplicationContext());
        allMusic = (ArrayList<Music>) database.musicDAO().getAllMusic();

        listAdapter = new MusicAdapter(this, R.layout.music_item, allMusic);
        setListAdapter(listAdapter);


}

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        String songTitle  = allMusic.get(position).title;
        int song = getResources().getIdentifier(songTitle, "raw", getPackageName());
        Intent intent = new Intent();
        intent.putExtra("song", song);
        intent.putExtra("songTitle", songTitle);
        setResult(RESULT_OK, intent);
        finish();

    }
}
