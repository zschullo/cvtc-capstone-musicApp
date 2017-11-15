package edu.cvtc.android.capstonemusic;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Beast on 11/13/17.
 */
@Entity
public class Genre {

    @PrimaryKey
    long id;
    String genreName;

    public Genre(long id, String genreName) {
        this.id = id;
        this.genreName = genreName;
    }
}
