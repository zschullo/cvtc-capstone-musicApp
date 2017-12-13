package edu.cvtc.android.capstonemusic;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class MyAppWidgetProvider extends AppWidgetProvider {

    public static String PLAY_ACTION = "PlayAction";

    /*
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.sound);
        mp.start();
    }
    */

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        //https://stackoverflow.com/questions/2748590/clickable-widgets-in-android

        final int count = appWidgetIds.length;

        for (int i = 0; i < count; i++) {

            int widgetId = appWidgetIds[i];

            // Create an Intent to launch ExampleActivity
            Intent intent = new Intent(context, MyAppWidgetProvider.class);
            intent.setAction(PLAY_ACTION);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

            // Get the layout for the App Widget and attach an on-click listener to the buttons
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);

            views.setOnClickPendingIntent(R.id.playButton, pendingIntent);
            //views.setOnClickPendingIntent(R.id.forwardButton, pendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(widgetId, views);

        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        //MediaPlayer mp = new MediaPlayer();

        super.onReceive(context, intent);

        if (intent.getAction().equals(PLAY_ACTION)) {



        }

    }








}
