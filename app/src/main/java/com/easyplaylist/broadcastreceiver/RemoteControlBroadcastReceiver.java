package com.easyplaylist.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;

import com.easyplaylist.engine.App;
import com.easyplaylist.services.PlayerService;

/**
 * Created by makar on 07/10/2014.
 */
public class RemoteControlBroadcastReceiver extends BroadcastReceiver {
//    public String componentName;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_MEDIA_BUTTON)) {
            final KeyEvent keyEvent = (KeyEvent) intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
            //ACTION_DOWN in xamarin
            if(keyEvent != null && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                String action = PlayerService.PLAY_PAUSE;
                if(keyEvent.getKeyCode() == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE){
                    Log.i(App.LOG_TAG,"RemoteControlBroadcastReceiver:KEYCODE_MEDIA_PLAY_PAUSE");
                    action = PlayerService.PLAY_PAUSE;
                } else if(keyEvent.getKeyCode() == KeyEvent.KEYCODE_MEDIA_PLAY){
                    Log.i(App.LOG_TAG,"RemoteControlBroadcastReceiver:KEYCODE_MEDIA_PLAY");
                    action = PlayerService.PLAY_PAUSE;
                } else if(keyEvent.getKeyCode() == KeyEvent.KEYCODE_MEDIA_STOP){
                    Log.i(App.LOG_TAG,"RemoteControlBroadcastReceiver:KEYCODE_MEDIA_STOP");
                    action = PlayerService.STOP;
                } else if(keyEvent.getKeyCode() == KeyEvent.KEYCODE_MEDIA_NEXT){
                    Log.i(App.LOG_TAG,"RemoteControlBroadcastReceiver:KEYCODE_MEDIA_NEXT");
                    action = PlayerService.NEXT;
                } else if(keyEvent.getKeyCode() == KeyEvent.KEYCODE_MEDIA_PREVIOUS){
                    Log.i(App.LOG_TAG,"RemoteControlBroadcastReceiver:KEYCODE_MEDIA_PREVIOUS");
                    action = PlayerService.PREVIOUS;
                }
                Intent remoteIntent = new Intent(action);
                context.startService(remoteIntent);
            }
        }



    }
}
