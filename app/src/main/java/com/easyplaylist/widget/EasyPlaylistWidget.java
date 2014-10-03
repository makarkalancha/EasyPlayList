package com.easyplaylist.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.easyplaylist.engine.App;
import com.easyplaylist.engine.R;
import android.util.Log;

/**
 * Created by makar on 03/10/2014.
 */
public class EasyPlaylistWidget extends AppWidgetProvider {


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
//        //TODO test
//        ComponentName thisWidget = new ComponentName(context, EasyPlaylistWidget.class);
//        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
//        for (int widgetId : appWidgetIds) {
////            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.view_player_widget);
//            Log.w(App.LOG_TAG, "appWidgetIds:" + widgetId);
////            remoteViews.setTextViewText(R.id.song_name, "bebebe: " + widgetId);
//        }
//        for (int widgetId : allWidgetIds) {
////            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.view_player_widget);
//            Log.w(App.LOG_TAG, "allWidgetIds:" + widgetId);
////            remoteViews.setTextViewText(R.id.song_name, "bebebe: " + widgetId);
//        }

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.view_player_widget);
        remoteViews.setOnClickPendingIntent(R.id.sync_button, buildButtonPendingIntent(context));
        remoteViews.setTextViewText(R.id.title,getTitle());
        remoteViews.setTextViewText(R.id.desc,getDesc());

        pushWidgetUpdate(context,remoteViews);
    }

    public static PendingIntent buildButtonPendingIntent(Context context){
        ++WidgetIntentReceiver.clickOut;
        Intent intent = new Intent();
        intent.setAction(WidgetIntentReceiver.ACTION);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static CharSequence getDesc() {
        return "Sync to see some of our funniest joke collections";
    }

    private static CharSequence getTitle() {
        return "Funny Jokes";
    }

    public static void pushWidgetUpdate(Context context, RemoteViews remoteViews) {
        ComponentName myWidget = new ComponentName(context, EasyPlaylistWidget.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(myWidget,remoteViews);
    }
}
