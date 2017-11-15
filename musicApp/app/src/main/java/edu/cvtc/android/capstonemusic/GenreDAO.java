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
public interface GenreDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addGenre(Genre genre);

//    @Query("select * from genre where musicId = :musicId")
//    List<Genre> findGenreForMusic(int musicId);

    @Query("select * from genre")
    List<Genre> getAllGenres();

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateGenre(Genre genre);

    @Query("delete from genre where id = :id")
    void delete(long id);

    @Query("delete from genre")
    void removeAllGenres();
}
