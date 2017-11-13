package edu.cvtc.android.capstonemusic;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by Beast on 11/13/17.
 */

@Dao
public interface MusicDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addMusic(Music music);

    @Query("select * from music")
    public List<Music> getAllMusic();

    @Query("select * from music where id = :musicId")
    public List<Music> getMusic(long musicId);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMusic(Music music);

    @Query("delete from music")
    void removeAllMusic();

}
