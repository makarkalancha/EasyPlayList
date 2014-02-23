package com.easyplaylist.engine;

import android.app.Application;
import android.content.res.Configuration;
import android.os.Environment;

public class App extends Application{
	private static App singleton;

//	public static final String MUSIC_PATH = "/storage/emulated/0/Music/!_test";
	public static final String MUSIC_PATH = Environment.getExternalStorageDirectory().toString()+"/Music/!_test";
    public static final String LOG_TAG = "LOG_EP>>>";
    public static Player _player;
//    public static int currentlyPlayingIndex = -1;

    public App getInstance(){
		return singleton;
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
	 
	@Override
	public void onCreate() {
		super.onCreate();
		singleton = this;
        _player = Player.getInstance();
    }
	 
	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}
	 
	@Override
	public void onTerminate() {
		super.onTerminate();
	}
}
