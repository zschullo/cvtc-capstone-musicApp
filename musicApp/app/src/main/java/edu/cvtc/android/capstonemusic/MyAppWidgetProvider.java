package edu.cvtc.android.capstonemusic;
import android.appwidget.AppWidgetProvider;
import android.util.Log;
import android.widget.RemoteViews;
import android.content.Context;
import android.appwidget.AppWidgetManager;

public class MyAppWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        final int count = appWidgetIds.length;

        for (int i = 0; i < count; i++) {

            int widgetId = appWidgetIds[i];

            Log.d("MSG", "Made it");

            // Retrieve the widget’s layout//
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);

            // Tell the AppWidgetManager to perform an update on this application widget//
            appWidgetManager.updateAppWidget(widgetId, views);

        }
    }


}
