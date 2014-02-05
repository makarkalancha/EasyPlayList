package com.easyplaylist.engine;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;

import java.util.concurrent.TimeUnit;

/**
 * Created by Makar on 2/4/14.
 */
public class Player {
    private MediaPlayer player;
    private double startTime = 0;
    private double endTime = 0;
    private Handler handler = new Handler();
    private long forwardTime = TimeUnit.SECONDS.toMillis(5);
    private long backwardTime = TimeUnit.SECONDS.toMillis(5);

    public Player(Context context, String mediaPath) {
        player = MediaPlayer.create(context, Uri.parse(mediaPath));
    }

    public MediaPlayer getPlayer() {
        return player;
    }

    public void setPlayer(MediaPlayer player) {
        this.player = player;
    }

    public double getStartTime() {
        return startTime;
    }

    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }

    public double getEndTime() {
        return endTime;
    }

    public void setEndTime(double endTime) {
        this.endTime = endTime;
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public long getForwardTime() {
        return forwardTime;
    }

    public void setForwardTime(long forwardTime) {
        this.forwardTime = forwardTime;
    }

    public long getBackwardTime() {
        return backwardTime;
    }

    public void setBackwardTime(long backwardTime) {
        this.backwardTime = backwardTime;
    }

}
