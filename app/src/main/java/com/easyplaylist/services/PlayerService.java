package com.easyplaylist.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.RemoteControlClient;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.RemoteViews;

import com.easyplaylist.UI.ActivityMain;
import com.easyplaylist.broadcastreceiver.RemoteControlBroadcastReceiver;
import com.easyplaylist.dao.Song;
import com.easyplaylist.engine.App;
import com.easyplaylist.engine.R;
import com.easyplaylist.widget.EasyPlaylistWidget;

import java.io.IOException;
import java.util.List;

/**
 * Created by makar on 02/10/2014.
 */
public class PlayerService extends Service
        implements
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener,
        AudioManager.OnAudioFocusChangeListener
{
    public static final String PREVIOUS = "com.easyplaylist.action.PREVIOUS";
    public static final String PLAY_PAUSE = "com.easyplaylist.action.PLAY_PAUSE";
    public static final String NEXT = "com.easyplaylist.action.NEXT";
    public static final String STOP = "com.easyplaylist.action.STOP";

    private static final int NOTIFY_ID = 1;
    private static boolean isRunning = false;

    private MediaPlayer _player;
    private List<Song> _songList;
    private int _currentlyPlayingIndex;
    private final IBinder _serviceBinder = new PlayerServiceBinder();

    private AudioManager _audioManager;
    private ComponentName _remoteComponentName;
    private RemoteControlClient _remoteControlClient;
    private boolean _paused;

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.i(App.LOG_TAG, "DefaultOnCompletionListener Player.play");
        forward(null);
        Intent intent = new Intent("updateUI");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();

        Song currentSong = getCurrentSong();

        Intent notIntent = new Intent(this, ActivityMain.class);
        //Intent.FLAG_ACTIVITY_CLEAR_TOP
        notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(this);
        builder
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.logo)
                .setTicker(currentSong.getArtist()+": "+currentSong.getTitle())
                .setOngoing(true)
                .setContentTitle(currentSong.getTitle())
                .setContentText(currentSong.getArtist());
        Notification notification = builder.build();
//                notification.flags = Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR;
        startForeground(NOTIFY_ID, notification);

        if(_remoteControlClient != null){
            _remoteControlClient.setPlaybackState(RemoteControlClient.PLAYSTATE_PLAYING);
        }
        updateMetadata();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        _currentlyPlayingIndex = -1;
        _player = new MediaPlayer();
        _audioManager = (AudioManager) getApplicationContext().getSystemService(getApplicationContext().AUDIO_SERVICE);
        _remoteComponentName = new ComponentName(getApplicationContext().getPackageName(), RemoteControlBroadcastReceiver.class.getName());
//        registerRemoteClient(this);
        initPlayer();
        Log.i(App.LOG_TAG, "PlayerService->onCreate");
        isRunning = true;
    }

    @Override
    public void onDestroy() {
        Log.i(App.LOG_TAG, "PlayerService->onDestroy");
        unregisterRemoteClient();
        isRunning = false;
        super.onDestroy();
    }

    private void initPlayer(){
        _player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        _player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        _player.setOnPreparedListener(this);
        _player.setOnCompletionListener(this);
        _player.setOnErrorListener(this);
    }

    public void playSong(final Context ctx, Song song){
        if(_player != null){
            _player.reset();
        }
        //// audio that's available as a local raw resource
//        _mediaPlayer = MediaPlayer.create(ctx, Uri.parse(song.getData()));
        ////URI available locally in the system (that you obtained through a Content Resolver
        try {
            _player.setDataSource(ctx, Uri.parse(song.getData()));
            _player.prepare();
//            _mediaPlayer.prepareAsync();


            int focusResult = _audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
            if(focusResult != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                Log.e(App.LOG_TAG, "Could not get audio focus");
            }
            registerRemoteClient(this);
            _remoteControlClient.setPlaybackState(RemoteControlClient.PLAYSTATE_BUFFERING);
//            updateMetadata();

        } catch (IOException e) {
            Log.e(App.LOG_TAG, "PlayerService->playSong exception", e);
        }
    }

    public void setSongList(List<Song> list){
        this._songList = list;
    }

    public int getCurrentlyPlayingIndex(){
        return _currentlyPlayingIndex;
    }

    public void setCurrentlyPlayingIndex(int currentlyPlayingIndex){
        _currentlyPlayingIndex = currentlyPlayingIndex;
    }

    public int getCurrentPosition(){
        return _player.getCurrentPosition();
    }

    public int getDuration(){
        return _player.getDuration();
    }

    public Song getCurrentSong() {
        return _songList.get(_currentlyPlayingIndex);
    }

    public void pause(){
        _player.pause();
    }

    public void forward(Context ctx){
        Log.i(App.LOG_TAG, "player forward");
        if(_songList.size() > 0){
            if(_currentlyPlayingIndex == _songList.size() - 1){
                _currentlyPlayingIndex = 0;
            }else{
                _currentlyPlayingIndex++;
            }
            playSong(ctx, _songList.get(_currentlyPlayingIndex));
        }
    }

    public void rewind(Context ctx){
        Log.i(App.LOG_TAG, "player rewind");
        if(_songList.size() > 0){
            if(_currentlyPlayingIndex == 0){
                _currentlyPlayingIndex = _songList.size()-1;
            }else{
                _currentlyPlayingIndex--;
            }
            playSong(ctx, _songList.get(_currentlyPlayingIndex));
        }
    }

    public void seekTo(int progress){
        _player.seekTo(progress);
    }

    public boolean isPlaying() {
        return _player.isPlaying();
    }

    public void start(){
        _player.start();
    }

    public void stop() {
        _player.stop();
        unregisterRemoteClient();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return _serviceBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        _player.stop();
        _player.release();

        return false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        if(action != null) {
            int appWidgetId = 0;
            Bundle bundle = intent.getExtras();
            if(bundle != null) {
                appWidgetId = bundle.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
            }

            Log.i(App.LOG_TAG, "onStartCommand called action:" + action + "; appWidgetId" + appWidgetId);

            if(PREVIOUS.equals(action)){
                rewind(getApplicationContext());
            } else if(NEXT.equals(action)){
                forward(getApplicationContext());
            } else if(PLAY_PAUSE.equals(action)){
                if(getCurrentlyPlayingIndex() > -1){
                    if(isPlaying()){
                        pause();
                    }else{
                        start();
                    }
                }else if(_songList.size() > 0){
                    playSong(this, _songList.get(0));
                    setCurrentlyPlayingIndex(0);
                }
            }

            AppWidgetManager manager = AppWidgetManager.getInstance(getApplicationContext());
            RemoteViews remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.view_player_widget);
            Song currentSong = getCurrentSong();
            remoteViews.setTextViewText(R.id.song_name, currentSong.getTitle());
            remoteViews.setTextViewText(R.id.artist_name, currentSong.getArtist());

            remoteViews.setOnClickPendingIntent(R.id.previous, EasyPlaylistWidget.buildPreviousPendingIntent(getApplicationContext(), appWidgetId));
//        remoteViews.setOnClickPendingIntent(R.id.play_pause,EasyPlaylistWidget.buildButtonPendingIntent(context));
//        remoteViews.setOnClickPendingIntent(R.id.next,EasyPlaylistWidget.buildButtonPendingIntent(context));

            manager.updateAppWidget(appWidgetId, remoteViews);
        }

//        return super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    private void registerRemoteClient(Context context) {
        try{
            if(_remoteControlClient == null) {

                _audioManager.registerMediaButtonEventReceiver(_remoteComponentName);
                Intent mediaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
                mediaButtonIntent.setComponent(_remoteComponentName);
                PendingIntent mediaPendingIntent = PendingIntent.getBroadcast(this, 0, mediaButtonIntent, 0);
                _remoteControlClient = new RemoteControlClient(mediaPendingIntent);
                _audioManager.registerRemoteControlClient(_remoteControlClient);
            }

            _remoteControlClient.setTransportControlFlags(
                    RemoteControlClient.FLAG_KEY_MEDIA_PLAY |
                    RemoteControlClient.FLAG_KEY_MEDIA_PAUSE |
                    RemoteControlClient.FLAG_KEY_MEDIA_PLAY_PAUSE |
                    RemoteControlClient.FLAG_KEY_MEDIA_STOP |
                    RemoteControlClient.FLAG_KEY_MEDIA_PREVIOUS |
                    RemoteControlClient.FLAG_KEY_MEDIA_NEXT
            );
        } catch (Exception e) {
            Log.e(App.LOG_TAG, "Exception", e);
        }
    }

    private void unregisterRemoteClient(){
        try {
            _audioManager.unregisterMediaButtonEventReceiver(_remoteComponentName);
            _audioManager.unregisterRemoteControlClient(_remoteControlClient);
            _remoteControlClient = null;
        } catch (Exception e) {
            Log.e(App.LOG_TAG, "Exception", e);
        }
    }

    private void updateMetadata(){
        if(_remoteControlClient != null) {
            Song currentSong = getCurrentSong();
            RemoteControlClient.MetadataEditor metadataEditor = _remoteControlClient.editMetadata(true);
            metadataEditor.putString(MediaMetadataRetriever.METADATA_KEY_ALBUM, currentSong.getAlbum());
//            metadataEditor.putString(MediaMetadataRetriever.METADATA_KEY_ARTIST, currentSong.getArtist());
            metadataEditor.putString(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST, currentSong.getArtist());
            metadataEditor.putString(MediaMetadataRetriever.METADATA_KEY_TITLE, currentSong.getTitle());

            Bitmap coverArt = currentSong.getAlbumart(this);
            metadataEditor.putBitmap(RemoteControlClient.MetadataEditor.BITMAP_KEY_ARTWORK, coverArt);
            metadataEditor.apply();
        }
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange){
            case AudioManager.AUDIOFOCUS_GAIN: {
                if (_player == null) {
                    initPlayer();
                }

                if (!isPlaying()){
                    start();
                    _paused = false;
                }

                _player.setVolume(1.0f,1.0f); //Turn it up
            };break;

            case AudioManager.AUDIOFOCUS_LOSS:{
                stop();
            };break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:{
                pause();
            };break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:{
                if(isPlaying()){
                    _player.setVolume(.1f, .1f); //turn it down!
                }
            };break;
        }
    }

    public class PlayerServiceBinder extends Binder {
        public PlayerService getService(){
            return PlayerService.this;
        }
    }

    public static boolean isRunning() {
        return PlayerService.isRunning;
    }

}
