package edu.cvtc.android.capstonemusic;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
/**
 * Created by Beast on 11/13/17.
 */

@Entity
public class Music {

    @PrimaryKey
    public final int id;
    public String title;
    public String artist;

    // Set this to 1 for YES 0 for NO
    public int unlocked;

    public Music(int id, String title, String artist, int unlocked) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.unlocked = unlocked;
    }
}
