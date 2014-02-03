package com.easyplaylist.engine;

import android.app.Application;
import android.content.res.Configuration;
import android.os.Environment;

public class App extends Application{
	private static App singleton;
	
//	public static final String MUSIC_PATH = "/storage/emulated/0/Music/!_test";
	public static final String MUSIC_PATH = Environment.getExternalStorageDirectory().toString()+"/Music/!_test";
	
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
