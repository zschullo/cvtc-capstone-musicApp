package edu.cvtc.android.capstonemusic;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Beast on 11/13/17.
 */

@Entity(tableName = "genre",
    foreignKeys = {
        @ForeignKey(
                entity = Music.class,
                parentColumns = "id",
                childColumns = "musicId",
                onDelete = ForeignKey.CASCADE
        )},
        indices = {@Index(value = "id")}
)

public class Genre {

    @PrimaryKey(autoGenerate = true)
    long id;
    public long musicId;

    String genreName;

    public Genre(long musicId, String genreName) {
        this.musicId = musicId;
        this.genreName = genreName;
    }
}
