package com.easyplaylist.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.WindowManager;
import android.widget.RemoteViews;

import com.easyplaylist.engine.R;
import com.easyplaylist.services.PlayerService;

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

//        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.view_player_widget);
//        remoteViews.setOnClickPendingIntent(R.id.sync_button, buildButtonPendingIntent(context));
//        remoteViews.setTextViewText(R.id.title,getTitle());
//        remoteViews.setTextViewText(R.id.desc,getDesc());


        for (int appWidgetId : appWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.view_player_widget);
            remoteViews.setOnClickPendingIntent(R.id.previous, buildPreviousPendingIntent(context, appWidgetId));
            remoteViews.setOnClickPendingIntent(R.id.next, buildNextPendingIntent(context, appWidgetId));
            remoteViews.setOnClickPendingIntent(R.id.play_pause, buildPlayPausePendingIntent(context, appWidgetId));
            remoteViews.setTextViewText(R.id.song_name, getTitle());
            remoteViews.setTextViewText(R.id.artist_name, getDesc());

            pushWidgetUpdate(context, remoteViews);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
//        Intent newIntent = new Intent(context, PlayerService.class);
//        newIntent
    }

    //    public static PendingIntent buildButtonPendingIntent(Context context){
//        ++WidgetIntentReceiver.clickOut;
//        Intent intent = new Intent();
//        intent.setAction(WidgetIntentReceiver.ACTION);
//        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//    }

    public static PendingIntent buildPreviousPendingIntent(Context context, int appWidgetId){
//        Intent intent = new Intent(context.getApplicationContext(), PlayerService.class);
////        Intent intent = new Intent();
//        intent.setAction(PlayerService.PREVIOUS);
//        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
////        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        return PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return buildPendingIntent(context, appWidgetId, PlayerService.PREVIOUS);
    }

    public static PendingIntent buildNextPendingIntent(Context context, int appWidgetId){
//        Intent intent = new Intent(context.getApplicationContext(), PlayerService.class);
////        Intent intent = new Intent();
//        intent.setAction(PlayerService.NEXT);
//        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
////        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        return PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return buildPendingIntent(context, appWidgetId, PlayerService.NEXT);
    }

    public static PendingIntent buildPlayPausePendingIntent(Context context, int appWidgetId) {
//        Intent intent = new Intent(context.getApplicationContext(), PlayerService.class);
////        Intent intent = new Intent();
//        intent.setAction(PlayerService.PLAY_PAUSE);
//        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
////        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        return PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return buildPendingIntent(context, appWidgetId, PlayerService.PLAY_PAUSE);
    }

    public static PendingIntent buildPendingIntent(Context context, int appWidgetId, String action){
        Intent intent = new Intent(context.getApplicationContext(), PlayerService.class);
//        Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
//        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

//    @Override
//    public void onAttachedToWindow() {
//        this.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
//                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
//                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
//                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN |
//                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
//                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
//                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
//    }

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
