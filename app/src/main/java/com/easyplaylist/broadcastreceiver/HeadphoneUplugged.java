package com.easyplaylist.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.easyplaylist.engine.App;
import com.easyplaylist.engine.Player;
import com.easyplaylist.interfaces.IUpdateUI;

/**
 * Created by Makar on 2/24/14.
 */
public class HeadphoneUplugged extends BroadcastReceiver{
    private IUpdateUI _updateUI;

    public HeadphoneUplugged(IUpdateUI updateUI) {
        _updateUI = updateUI;
    }

    public HeadphoneUplugged() { }
    @Override
    public void onReceive(Context context, Intent intent) {
//        if(intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
//            Player player = App._player;
//            int state = intent.getIntExtra("state", -1);
//            switch (state) {
//                case 0:
//                    Log.i(App.LOG_TAG, "Headset is unplugged and state 0");
//                    player.pause();
//                    _updateUI.update();
//                    break;
//                case 1:
//                    Log.i(App.LOG_TAG, "Headset is plugged and state 1");
//                    player.pause();
//                    _updateUI.update();
//                    break;
//                default:
//                    Log.i(App.LOG_TAG, "state default " + state);
//                    break;
//            }
//        }
        Log.i(App.LOG_TAG, "HeadphoneUplugged>>>DO NOTHING<<<");
    }
}
