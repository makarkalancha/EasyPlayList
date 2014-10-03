package com.easyplaylist.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.easyplaylist.engine.R;

/**
 * Created by makar on 03/10/2014.
 */
public class WidgetIntentReceiver extends BroadcastReceiver {
    public static int clickOut = 0;
    private String[] msg = null;
    public static final String ACTION = "com.easyplaylist.widget.UPDATE";

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(ACTION)){
            updateWidgetPictureAndButtonListener(context);
        }
    }

    private void updateWidgetPictureAndButtonListener(Context context) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.view_player_widget);
        remoteViews.setTextViewText(R.id.title,getTitle());
        remoteViews.setTextViewText(R.id.desc,getDesc(context));

        remoteViews.setOnClickPendingIntent(R.id.sync_button,EasyPlaylistWidget.buildButtonPendingIntent(context));

        EasyPlaylistWidget.pushWidgetUpdate(context.getApplicationContext(),remoteViews);
    }

    private CharSequence getDesc(Context context) {
        msg = context.getResources().getStringArray(R.array.news_headlines);
        if(clickOut >= msg.length) {
            clickOut = 0;
        }
        return msg[clickOut];
    }

    private CharSequence getTitle() {
        return "Funny Jokes";
    }
}
