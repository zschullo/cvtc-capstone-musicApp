package edu.cvtc.android.capstonemusic;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.MapView;

import java.io.Console;
import java.lang.reflect.Field;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private ImageButton playButton;
    private ImageButton fastForwardButton;
    private ImageButton reverseButton;
    private ImageButton mapButton;

    private TextView timeLabel;
    private TextView totalTimeLabel;

    private SeekBar seekBar;
    private Toast toast = null;
    private MediaPlayer mediaPlayer = null;

    // Database Impl
    private Music music;
    private AppDatabase database;
    private Genre genre;
    private Handler mHandler = new Handler();


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createDatabase();

        // Gets the id after the main activity is created.
        playButton = (ImageButton) findViewById(R.id.playButton);
        fastForwardButton = (ImageButton) findViewById(R.id.fastForwardButton);
        reverseButton = (ImageButton) findViewById(R.id.reverseButton);
        mapButton = (ImageButton) findViewById(R.id.mapButton);
        seekBar = (SeekBar) findViewById(R.id.musicBar);
        timeLabel = (TextView) findViewById(R.id.timeInitial);
        totalTimeLabel = (TextView) findViewById(R.id.timeTotal);

        // Sets Listeners
        playButton.setOnClickListener(this);
        fastForwardButton.setOnClickListener(this);
        reverseButton.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(this);
        mapButton.setOnClickListener(this);


        setupMusic(R.raw.arma_puros_plus_nothing_else);

        MainActivity.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {


                if (mediaPlayer != null) {
                    int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                    seekBar.setProgress(mCurrentPosition);
                }
                mHandler.postDelayed(this, 1000);
            }
        });



    }

    // This will get fired off when you click play and any other button.
    @Override
    public void onClick(View view) {

        if (view == playButton) {
            displayToast("You've clicked play");

            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                playButton.setImageResource(R.drawable.play);
            } else {
                mediaPlayer.start();
                playButton.setImageResource(R.drawable.pause);
            }
        } else if (view == mapButton) {
            displayToast("The map button was pressed");
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
        }


    }

    private void displayToast(String message) {

        // Will prevent toast from showing if tapping the button a lot.
        if (toast == null || toast.getView().getWindowVisibility() != View.VISIBLE) {
            toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
            toast.show();
        }
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (mediaPlayer != null && fromUser) {
            mediaPlayer.seekTo(progress * 1000);
        }

        int minute = progress / 60;
        int second = progress % 60;
        String secondFormatted = "";
        if (second < 10 ) {

           secondFormatted = "0" + second;
        } else {
            secondFormatted = "" + second;
        }
        String time = minute + ":" + secondFormatted;
        timeLabel.setText(time);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            playButton.setImageResource(R.drawable.play);

        }

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        

    }

    @Override
    protected void onDestroy() {
        AppDatabase.destroyInstance();
        super.onDestroy();
    }

    public void setupMusic(int song) {
        mediaPlayer = MediaPlayer.create(this, song);


        seekBar.setProgress(0);
        seekBar.setMax(mediaPlayer.getDuration() / 1000);
        int minute = mediaPlayer.getDuration() / 60000;
        int second = (mediaPlayer.getDuration() % 60000) / 1000;
        String secondFormatted = "";
        if (second < 10 ) {

            secondFormatted = "0" + second;
        } else {
            secondFormatted = "" + second;
        }
        String time = minute + ":" + secondFormatted;
        totalTimeLabel.setText(time);

    }

    public void createDatabase() {
        database = AppDatabase.getDatabase(getApplicationContext());

        database.genreDAO().removeAllGenres();
        database.musicDAO().removeAllMusic();

        // Adds a new Genre to the table.
        database.genreDAO().addGenre(new Genre(1, "Rock"));
        database.genreDAO().addGenre(new Genre(2, "Metal"));
        database.genreDAO().addGenre(new Genre(3, "Classical"));
        database.genreDAO().addGenre(new Genre(4, "Jazz"));

        genre = database.genreDAO().getAllGenres().get(3);
        //displayToast(genre.genreName);

        MediaMetadataRetriever songMetaData = new MediaMetadataRetriever();

        Field[] fields = R.raw.class.getFields();

        for (int count = 0; count < fields.length; count++) {

            // The AssetFileDescriptor will get the song from the raw folder.
            // This will then let us use the song's metadata to populate the database.
            AssetFileDescriptor assetFileDescriptor = null;
            try {

                assetFileDescriptor = getResources().openRawResourceFd(fields[count].getInt(null));

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            songMetaData.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());

            String artist = songMetaData.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);

            // Quick and dirty way to assign genreId so we don't have to
            // manually enter it.
            final int random = new Random().nextInt((4 - 1) + 1) + 1;

            String songName = fields[count].getName() + ".mp3";

            database.musicDAO().addMusic(new Music(count,
                    songName,
                    artist,
                    random,
                    0));

            music = database.musicDAO().getMusic(count).get(0);
            //displayToast("Added " + music.title + ", GenreId: " + music.genreId);
        }

    }
}
