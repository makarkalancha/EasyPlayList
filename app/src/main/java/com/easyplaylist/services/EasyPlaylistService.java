package com.easyplaylist.services;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;

import com.easyplaylist.dao.Song;
import com.easyplaylist.engine.Player;

import java.util.ArrayList;

/**
 * Created by makar on 29/09/2014.
 * http://code.tutsplus.com/tutorials/create-a-music-player-on-android-song-playback--mobile-22778
 */
public class EasyPlaylistService extends Service
        implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener
{
    private Player _player;
    private ArrayList<Song> _songs;
    private int _songPositionNum;

    @Override
    public void onCreate() {
        super.onCreate();
        _songPositionNum = 0;
        _player = Player.getInstance();
        initMusicPlayer();
    }

    public void initMusicPlayer(){
        _player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        _player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        _player.setOnPreparedListener(this);
        _player.setOnCompletionListener(this);
        _player.setOnErrorListener(this);
    }

    public void setList(ArrayList<Song> songs) {
        _songs = songs;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

    }

    public class EasyPlaylistBinder extends Binder{
        EasyPlaylistService getService() {
            return EasyPlaylistService.this;
        }

    }
}
