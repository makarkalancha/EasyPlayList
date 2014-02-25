package com.easyplaylist.engine;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import com.easyplaylist.data.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Makar on 2/16/14.
 */
public class Player {
    private static Player playerInstance;

    private MediaPlayer _mediaPlayer;
    private double _startTime = 0;
    private double _endTime = 0;
    private List<Song> _songList = new ArrayList<Song>();
    private App _appInst;
    private int _currentlyPlayingIndex = -1;
    private MediaPlayer.OnCompletionListener _onCompletionListener = new DefaultOnCompletionListener();

    private Player() {
        _mediaPlayer = new MediaPlayer();
        _appInst = new App();
        Log.i(_appInst.LOG_TAG, "Player.default constructor");
    }

    public static Player getInstance() {
        return PlayerHolder.player;
    }

    private static class PlayerHolder{
        private static Player player = new Player();
    }

    public int getCurrentPosition(){
        return _mediaPlayer.getCurrentPosition();
    }

    public Song getCurrentSong() {
        return _songList.get(_currentlyPlayingIndex);
    }

    public int getCurrentlyPlayingIndex(){
        return _currentlyPlayingIndex;
    }

    public void setCurrentlyPlayingIndex(int currentlyPlayingIndex){
        _currentlyPlayingIndex = currentlyPlayingIndex;
    }

    public int getDuration(){
        return _mediaPlayer.getDuration();
    }

    public void playSong(final Context ctx, Song song){
        if(_mediaPlayer != null){
            _mediaPlayer.reset();
        }
        _mediaPlayer = MediaPlayer.create(ctx, Uri.parse(song.getData()));
        _mediaPlayer.start();
        _mediaPlayer.setOnCompletionListener(_onCompletionListener);
    }

    public void pause(){
        _mediaPlayer.pause();
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
        _mediaPlayer.seekTo(progress);
    }

    public boolean isPlaying() {
        return _mediaPlayer.isPlaying();
    }

    public void start(){
        _mediaPlayer.start();
    }

    public void reset(){
        _mediaPlayer.reset();
    }

    public void release(){
        _mediaPlayer.release();
    }

    public void setSongsList(List<Song> songs) {
        _songList = songs;
    }

    public Player withOnCompletionListener(MediaPlayer.OnCompletionListener onCompletionListener){
        _onCompletionListener = onCompletionListener;
        return this;
    }

    class DefaultOnCompletionListener implements MediaPlayer.OnCompletionListener{
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            Log.i(App.LOG_TAG, "DefaultOnCompletionListener Player.play");
            forward(null);
        }
    }
}
