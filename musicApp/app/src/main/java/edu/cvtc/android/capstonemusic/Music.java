package edu.cvtc.android.capstonemusic;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
/**
 * Created by Beast on 11/13/17.
 */
@Entity(tableName = "music",
        foreignKeys = {
                @ForeignKey(
                        entity = Genre.class,
                        parentColumns = "id",
                        childColumns = "genreId",
                        onDelete = ForeignKey.CASCADE
                )},
        indices = {@Index(value = "id"), @Index(value = "genreId")}
)

public class Music {

    @PrimaryKey
    public final int id;
    public String title;
    public String artist;
    public int genreId;

    // Set this to 1 for YES 0 for NO
    public int unlocked;

    public Music(int id, String title, String artist, int genreId, int unlocked) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.unlocked = unlocked;
        // Should be a foreign key.
        this.genreId = genreId;
    }
}
