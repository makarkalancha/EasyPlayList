package com.easyplaylist.listeners;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.easyplaylist.engine.App;
import com.easyplaylist.engine.Player;
import com.easyplaylist.interfaces.IUpdateUI;

/**
 * Created by Makar on 2/24/14.
 */
public class IncomingCallListener extends PhoneStateListener {
    private IUpdateUI _updateUI;

    public IncomingCallListener(IUpdateUI updateUI) {
        _updateUI = updateUI;
    }
    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        Player player = App._player;
        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:
                Log.i(App.LOG_TAG, "Incoming call");
                player.pause();
                break;
        }
    }
}
