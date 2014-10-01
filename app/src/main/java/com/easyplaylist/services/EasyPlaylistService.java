package com.easyplaylist.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;

import com.easyplaylist.UI.ActivityMain;
import com.easyplaylist.dao.Song;
import com.easyplaylist.engine.Player;
import com.easyplaylist.engine.R;

import java.util.List;

/**
 * Created by makar on 29/09/2014.
 * http://code.tutsplus.com/tutorials/create-a-music-player-on-android-song-playback--mobile-22778
 */
public class EasyPlaylistService extends Service
//        implements
//        MediaPlayer.OnPreparedListener,
//        MediaPlayer.OnErrorListener,
//        MediaPlayer.OnCompletionListener
{
    private Player _player;
    private List<Song> _songs;
    private int _songPositionNum;
    private final IBinder _serviceBind = new EasyPlaylistBinder();
    private static final int NOTIFY_ID = 1;

    @Override
    public void onCreate() {
        super.onCreate();
        _songPositionNum = 0;
        _player = Player.getInstance();
        initMusicPlayer();

//        Intent notIntent = new Intent(this, ActivityMain.class);
//        notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        Notification.Builder builder = new Notification.Builder(this);
//        builder
//                .setContentIntent(pendingIntent)
//                .setSmallIcon(R.drawable.logo)
//                .setTicker(_currentSongTitle)
//                .setOngoing(true)
//                .setContentTitle("Playing")
//                .setContentText(_currentSongTitle);
//        Notification notification = builder.build();
//        startForeground(NOTIFY_ID,notification);

    }

    public void initMusicPlayer(){
        _player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        _player.withOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                _player.start();

                Song currentlyPlayingSong = _player.getCurrentSong();
                String currentSongTitle = currentlyPlayingSong.getName();

                Intent notIntent = new Intent(EasyPlaylistService.this, ActivityMain.class);
                //Intent.FLAG_ACTIVITY_CLEAR_TOP
                notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(EasyPlaylistService.this, 0, notIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_NO_CREATE);
                Notification.Builder builder = new Notification.Builder(EasyPlaylistService.this);
                builder
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.drawable.logo)
                        .setTicker(currentSongTitle)
                        .setOngoing(true)
                        .setContentTitle("Playing")
                        .setContentText(currentSongTitle);
                Notification notification = builder.build();
//                notification.flags = Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR;
                startForeground(NOTIFY_ID, notification);
            }
        });
//        _player.setAudioStreamType(AudioManager.STREAM_MUSIC);
//        _player.setOnPreparedListener(this);
//        _player.setOnCompletionListener(this);
//        _player.setOnErrorListener(this);
    }

    public void setList(List<Song> songs) {
        _songs = songs;
    }

    public void setSongPositionNum(int songIndex) {
        _songPositionNum = songIndex;
    }

//    public void setCurrentSong(Song song){
//        _currentlyPlayingSong = song;
//        _currentSongTitle = _currentlyPlayingSong.getName();
//    }

//    public void playSong(){
////        _player.reset();
//        Song songToPlay = _songs.get(_songPositionNum);
//        _player.playSong(getApplicationContext(), songToPlay);
//    }

    @Override
    public IBinder onBind(Intent intent) {
        return _serviceBind;
    }

    @Override
    public boolean onUnbind(Intent intent) {
//        _player.stop();
//        _player.release();
        return false;
    }

//    @Override
//    public void onCompletion(MediaPlayer mp) {
//
//    }
//
//    @Override
//    public boolean onError(MediaPlayer mp, int what, int extra) {
//        return false;
//    }

//    @Override
//    public void onPrepared(MediaPlayer mp) {
//        mp = _player.getMediaPlayer();
//        mp.start();
//    }

    public class EasyPlaylistBinder extends Binder{
        public EasyPlaylistService getService() {
            return EasyPlaylistService.this;
        }

    }
}
