package edu.cvtc.android.capstonemusic;

import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private ImageButton playButton;
    private ImageButton fastForwardButton;
    private ImageButton reverseButton;

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




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createDatabase();

        // Gets the id after the main activity is created.
        playButton = (ImageButton) findViewById(R.id.playButton);
        fastForwardButton = (ImageButton) findViewById(R.id.fastForwardButton);
        reverseButton = (ImageButton) findViewById(R.id.reverseButton);
        seekBar = (SeekBar) findViewById(R.id.musicBar);
        timeLabel = (TextView) findViewById(R.id.timeInitial);
        totalTimeLabel = (TextView) findViewById(R.id.timeTotal);

        // Sets Listeners
        playButton.setOnClickListener(this);
        fastForwardButton.setOnClickListener(this);
        reverseButton.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(this);


        setupMusic(R.raw.arma_puros_plus_nothing_else);

        MainActivity.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {



                if(mediaPlayer != null){
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
            String time = minute + ":" + second;
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
        seekBar.setMax(mediaPlayer.getDuration()/1000);
        int minute = mediaPlayer.getDuration() / 60000;
        int second = (mediaPlayer.getDuration() % 60000) / 1000;
        String time = minute + ":" + second;
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
        displayToast(genre.genreName);

        database.musicDAO().addMusic(new Music(1,
                "arma_puros_plus_nothing_else.mp3",
                "Zach",
                2,
                0));

        music = database.musicDAO().getMusic(1).get(0);
        displayToast("Added " + music.title + ", GenreId: " + music.genreId);



        // TODO: Remove after testing.
        // Clean up for testing purposes.
        //database.musicDAO().removeAllMusic();

        // Add test data.
        //List<Music> musicList = database.musicDAO().getAllMusic();



//        if (musicList.size() == 0) {
//            database.musicDAO().addMusic(new Music(1, "Happy Birthday", "Everyone", 0));
//            music = database.musicDAO().getAllMusic().get(0);
//            displayToast(music.title);
//        }
    }

}
