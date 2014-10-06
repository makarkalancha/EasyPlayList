package com.easyplaylist.services;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.easyplaylist.engine.App;

/**
 * Created by makar on 06/10/2014.
 */
@Deprecated
public class PlayerConnection implements ServiceConnection {
    private PlayerService _playerService;

    public PlayerConnection(PlayerService playerService) {
        this._playerService = playerService;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        PlayerService.PlayerServiceBinder binder = (PlayerService.PlayerServiceBinder) service;
        //get service
//        _playerService = binder.getService();
//        _playerService.setSongList(songs);
//        Log.d(App.LOG_TAG, "onServiceConnected");
//        musicBound = true;
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        Log.d(App.LOG_TAG, "onServiceDisconnected");
//        musicBound = false;
    }
}
