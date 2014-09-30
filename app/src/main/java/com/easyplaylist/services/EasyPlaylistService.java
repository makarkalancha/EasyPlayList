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
import java.util.List;

/**
 * Created by makar on 29/09/2014.
 * http://code.tutsplus.com/tutorials/create-a-music-player-on-android-song-playback--mobile-22778
 */
public class EasyPlaylistService extends Service
        implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener
{
    private Player _player;
    private List<Song> _songs;
    private int _songPositionNum;
    private final IBinder _serviceBind = new EasyPlaylistBinder();

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

    public void setList(List<Song> songs) {
        _songs = songs;
    }

    public void setSongPositionNum(int songIndex) {
        _songPositionNum = songIndex;
    }

    public void playSong(){
        _player.reset();
        Song songToPlay = _songs.get(_songPositionNum);
        _player.playSong(getApplicationContext(), songToPlay);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return _serviceBind;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        _player.stop();
        _player.release();
        return false;
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
        mp = _player.getMediaPlayer();
        mp.start();
    }

    public class EasyPlaylistBinder extends Binder{
        public EasyPlaylistService getService() {
            return EasyPlaylistService.this;
        }

    }
}
