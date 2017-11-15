package edu.cvtc.android.capstonemusic;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private ImageButton playButton;
    private ImageButton fastForwardButton;
    private ImageButton reverseButton;

    private SeekBar seekBar;

    private Toast toast = null;
    private MediaPlayer mediaPlayer = null;

    // Database Impl
    private Music music;
    private AppDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = AppDatabase.getDatabase(getApplicationContext());

        // TODO: Remove after testing.
        // Clean up for testing purposes.
        database.musicDAO().removeAllMusic();

        // Add test data.
        List<Music> musicList = database.musicDAO().getAllMusic();

        if (musicList.size() == 0) {
            database.musicDAO().addMusic(new Music(1, "Happy Birthday", "Everyone", 0));
            music = database.musicDAO().getAllMusic().get(0);
            displayToast(music.title);
        }

        // Gets the id after the main activity is created.
        playButton = (ImageButton) findViewById(R.id.playButton);
        fastForwardButton = (ImageButton) findViewById(R.id.fastForwardButton);
        reverseButton = (ImageButton) findViewById(R.id.reverseButton);
        seekBar = (SeekBar) findViewById(R.id.musicBar);
        // Sets Listeners
        playButton.setOnClickListener(this);
        fastForwardButton.setOnClickListener(this);
        reverseButton.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(this);
        setupMusic(R.raw.defaultsong);


    }

    // This will get fired off when you click play and any other button.
    @Override
    public void onClick(View view) {

        if (view == playButton) {
            displayToast("You've clicked play");

            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                playButton.setImageResource(R.drawable.play);
            } else {
                mediaPlayer.start();
                playButton.setImageResource(R.drawable.pause);
            }



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
        mediaPlayer.seekTo(progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            playButton.setImageResource(R.drawable.play);
        }

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public void setupMusic(int song) {
        mediaPlayer = MediaPlayer.create(this, song);


        seekBar.setProgress(0);
        seekBar.setMax(mediaPlayer.getDuration());
    }

    
}
