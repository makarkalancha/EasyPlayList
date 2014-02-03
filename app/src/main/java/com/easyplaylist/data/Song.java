package com.easyplaylist.data;

import java.io.File;
import java.io.FileDescriptor;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

public class Song {
	private long _id;
	private String _data;
	private String _name;
	private String _duration;
	private long _album_id;
	
	public Song() {}
	public Song(long id, String data, String name, String duration, long album) {
		this._id = id;
		this._data = data;
		this._name = name;
		this._duration = duration;
		this._album_id = album;
	}
	
	public long getId() {
		return _id;
	}
	public void setId(long id) {
		this._id = id;
	}
	public String getData() {
		return _data;
	}
	public void setData(String data) {
		this._data = data;
	}
	public String getName() {
		return _name;
	}
	public void setName(String name) {
		this._name = name;
	}
	public String getDuration() {
		return _duration;
	}
	public void setDuration(String duration) {
		this._duration = duration;
	}
	
	public long get_album_id() {
		return _album_id;
	}
	public void set_album_id(long _album_id) {
		this._album_id = _album_id;
	}
	
	public Bitmap getAlbumart(Context context){
	    Bitmap bm = null;
	    try 
	    {
	        final Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");

	        Uri uri = ContentUris.withAppendedId(sArtworkUri, this.get_album_id());

	        ParcelFileDescriptor pfd = context.getContentResolver()
	            .openFileDescriptor(uri, "r");

	        if (pfd != null) 
	        {
	            FileDescriptor fd = pfd.getFileDescriptor();
	            bm = BitmapFactory.decodeFileDescriptor(fd);
	        }
	} catch (Exception e) {
	}
	return bm;
	}
}
