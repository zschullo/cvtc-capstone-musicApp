package edu.cvtc.android.capstonemusic;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by Nicholas on 11/29/17.
 */

public class MusicAdapter extends ArrayAdapter<Music> {
    private ArrayList<Music> objects;
    public MusicAdapter(Context context, int textViewResourceId, ArrayList<Music> objects) {
        super(context, textViewResourceId, objects);
        this.objects = objects;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Music music = objects.get(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.music_item, parent, false);
        }
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView author = (TextView) convertView.findViewById(R.id.author);
        title.setText(music.title);
        author.setText(music.artist);
        if (music.unlocked == 1){
            title.setTextColor(Color.GREEN);
            author.setTextColor(Color.GREEN);
        } else {
            title.setTextColor(Color.RED);
            author.setTextColor(Color.RED);
        }





        return convertView;
    }
}
