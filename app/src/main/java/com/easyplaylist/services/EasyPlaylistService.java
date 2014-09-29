package com.easyplaylist.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by makar on 29/09/2014.
 * http://code.tutsplus.com/tutorials/create-a-music-player-on-android-song-playback--mobile-22778
 */
public class EasyPlaylistService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
