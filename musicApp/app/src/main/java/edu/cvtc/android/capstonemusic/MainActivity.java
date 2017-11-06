package edu.cvtc.android.capstonemusic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    private ImageButton playButton = (ImageButton) findViewById(R.id.playButton);
    private ImageButton stopButton = (ImageButton) findViewById(R.id.stopButton);
    private ImageButton fastForwardButton = (ImageButton) findViewById(R.id.fastForwardButton);
    private ImageButton reverseButton = (ImageButton) findViewById(R.id.reverseButton);
    private ImageButton muteButton = (ImageButton) findViewById(R.id.muteButton);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
