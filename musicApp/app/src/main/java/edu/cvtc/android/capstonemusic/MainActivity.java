package edu.cvtc.android.capstonemusic;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // TODO: Remove unused buttons after Drew removes them.
    private ImageButton playButton;
    //private ImageButton stopButton;
    private ImageButton fastForwardButton;
    private ImageButton reverseButton;
    //private ImageButton muteButton;
    private Toast toast = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Gets the id after the main activity is created.
        playButton = (ImageButton) findViewById(R.id.playButton);
        //stopButton = (ImageButton) findViewById(R.id.stopButton);
        fastForwardButton = (ImageButton) findViewById(R.id.fastForwardButton);
        reverseButton = (ImageButton) findViewById(R.id.reverseButton);
        //muteButton = (ImageButton) findViewById(R.id.muteButton);

        playButton.setOnClickListener(this);
        fastForwardButton.setOnClickListener(this);

    }

    // This will get fired off when you click play.
    @Override
    public void onClick(View view) {

        if (view == fastForwardButton) {
            displayToast("You've clicked play");
        }else{
            displayToast("You've clicked fast forward");
        }


    }

    private void displayToast(String message) {

        // Will prevent toast from showing if tapping the button a lot.
        if (toast == null || toast.getView().getWindowVisibility() != View.VISIBLE) {
            toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
