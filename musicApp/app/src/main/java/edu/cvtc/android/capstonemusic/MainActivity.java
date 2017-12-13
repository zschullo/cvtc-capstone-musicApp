package edu.cvtc.android.capstonemusic;

import android.*;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.tv.TvContract;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private ImageButton playButton;
    private ImageButton fastForwardButton;
    private ImageButton reverseButton;

    private static final int SONG_LIST_RESULT_CODE = 0;
    private ImageView songImage;

    private ImageButton listButton;

    private ImageButton mapButton;

    private ProgressBar progressBar;

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
    private String currentSong;

    LocationManager locationManager;
    private Location lastLocation;
    private Location currentLocation;
    private float distanceTraveled;
    private LatLng latLng;
    private int progress;
    LocationListener locationListener;

    // Music Impl
    MediaMetadataRetriever songMetaData = new MediaMetadataRetriever();
    byte[] rawArt;
    Bitmap bitmap = null;
    BitmapFactory.Options options = new BitmapFactory.Options();


    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getPermissionToUseLocationServices();

        songImage = (ImageView) findViewById(R.id.imageView);

        // Gets the id after the main activity is created.
        playButton = (ImageButton) findViewById(R.id.playButton);
        fastForwardButton = (ImageButton) findViewById(R.id.fastForwardButton);
        reverseButton = (ImageButton) findViewById(R.id.reverseButton);
        mapButton = (ImageButton) findViewById(R.id.mapButton);
        seekBar = (SeekBar) findViewById(R.id.musicBar);
        timeLabel = (TextView) findViewById(R.id.timeInitial);
        totalTimeLabel = (TextView) findViewById(R.id.timeTotal);
        listButton = (ImageButton) findViewById(R.id.listButton);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);


        // Sets Listeners
        playButton.setOnClickListener(this);
        fastForwardButton.setOnClickListener(this);
        reverseButton.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(this);


        createDatabase();

        mapButton.setOnClickListener(this);

        listButton.setOnClickListener(this);


        setupMusic(R.raw.insane);
        currentSong = "insane";

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

        locationManager = (LocationManager) getSystemService(this.LOCATION_SERVICE);

        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                lastLocation = currentLocation;
                currentLocation = location;

                if (lastLocation != null && currentLocation != null) {
                    distanceTraveled = lastLocation.distanceTo(location);
                }

                latLng = new LatLng(location.getLatitude(), location.getLongitude());

                //if (location.getSpeed() > 5) {
                updateProgressBar();
                //}
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 1, locationListener);

    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(locationListener);
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 1, locationListener);
    }

    // Identifier for the permission request
    private static final int ACCESS_FINE_LOCATION_PERMISSION_REQUEST = 1;

    // Called when the user is performing an action which requires the app to read the
    // user's contacts
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getPermissionToUseLocationServices() {
        // 1) Use the support library version ContextCompat.checkSelfPermission(...) to avoid
        // checking the build version since Context.checkSelfPermission(...) is only available
        // in Marshmallow
        // 2) Always check for permission (even if permission has already been granted)
        // since the user can revoke permissions at any time through Settings
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // The permission is NOT already granted.
            // Check if the user has been asked about this permission already and denied
            // it. If so, we want to give more explanation about why the permission is needed.
            if (shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show our own UI to explain to the user why we need to read the contacts
                // before actually requesting the permission and showing the default UI
            }

            // Fire off an async request to actually get the permission
            // This will show the standard permission request dialog UI
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION_PERMISSION_REQUEST);

        }

    }

    // Callback with the request from calling requestPermissions(...)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {

        if (requestCode == ACCESS_FINE_LOCATION_PERMISSION_REQUEST) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                displayToast("Location permission granted");
            } else {
                // showRationale = false if user clicks Never Ask Again, otherwise true
                boolean showRationale = shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION);

                if (showRationale) {

                } else {
                    displayToast("Location permission denied");
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void updateProgressBar() {
        if (progress >= 1610) {
            progress = progress - 1610;
            progressBar.setProgress(progress);
            displayToast("A new song was unlocked!");
        } else {
            progress += Math.round(distanceTraveled);
            progressBar.setProgress(progress);
        }
    }

    // This will get fired off when you click play and any other button.
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View view) {

        if (view == playButton) {

            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                playButton.setImageResource(R.drawable.play);
            } else {
                mediaPlayer.start();
                playButton.setImageResource(R.drawable.pause);
            }
        } else if (view == mapButton) {
            Intent intent = new Intent(this, MapsActivity.class);
            intent.putExtra("progress", progress);
            launchActivity(MapsActivity.class);
        } else if (view == listButton) {
            mediaPlayer.release();
            mediaPlayer = null;
            Intent intent = new Intent(this, SongListActivity.class);
            startActivityForResult(intent, SONG_LIST_RESULT_CODE);


        } else if (view == fastForwardButton) {
            mediaPlayer.release();
            mediaPlayer = null;
            List<Music> allMusic = database.musicDAO().getAllMusic();
            Music correctSong = null;
            for (Music song:allMusic){
                if (Objects.equals(song.title, currentSong)) {
                    if (song.id < allMusic.size() - 1) {
                        correctSong = database.musicDAO().getMusic(song.id + 1).get(0);
                    } else {
                        correctSong = database.musicDAO().getMusic(0).get(0);
                    }
                }
            }
            currentSong = correctSong.title;
            setupMusic(getResources().getIdentifier(correctSong.title, "raw", getPackageName()));
        } else if (view == reverseButton) {
            mediaPlayer.release();
            mediaPlayer = null;
            List<Music> allMusic = database.musicDAO().getAllMusic();
            Music correctSong = null;
            for (Music song:allMusic){
                if (Objects.equals(song.title, currentSong)) {
                    if (song.id > 0) {
                        correctSong = database.musicDAO().getMusic(song.id - 1).get(0);
                    } else {
                        correctSong = database.musicDAO().getMusic(allMusic.size() - 1).get(0);
                    }
                }
            }
            currentSong = correctSong.title;
            setupMusic(getResources().getIdentifier(correctSong.title, "raw", getPackageName()));
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // check that it is the SecondActivity with an OK result
        if (requestCode == SONG_LIST_RESULT_CODE) {
            if (resultCode == RESULT_OK) {

                int song = data.getIntExtra("song",R.raw.plus_nothing_else);
                currentSong = data.getStringExtra("songTitle");
                setupMusic(song);
                mediaPlayer.start();
            }
        }
    }
    private void launchActivity(Class activity) {

        Intent intent = new Intent(this, activity);
        startActivity(intent);
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
        mediaPlayer = null;
        mediaPlayer = MediaPlayer.create(this, song);

        displayImage(song);

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

            String songName = fields[count].getName();

            database.musicDAO().addMusic(new Music(count,
                    songName,
                    artist,
                    random,
                    0));

            music = database.musicDAO().getMusic(count).get(0);
        }

    }

    private void displayImage(int song) {

        AssetFileDescriptor assetFileDescriptor;
        assetFileDescriptor = getResources().openRawResourceFd(song);

        songMetaData.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());

        // This will get the image from the meta data and convert it to a Drawable object.
        // It will then set the main image.
        if (songMetaData.getEmbeddedPicture() != null) {
            rawArt = songMetaData.getEmbeddedPicture();
            bitmap = BitmapFactory.decodeByteArray(rawArt, 0, rawArt.length, options);

            Drawable drawable = new BitmapDrawable(getResources(), bitmap);

            songImage.setImageDrawable(drawable);

        } else {
            try {
                songImage.setImageResource(R.drawable.placeholder);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
