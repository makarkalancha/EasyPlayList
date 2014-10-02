package com.easyplaylist.dao;

import java.io.FileDescriptor;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

public class Song {
	private long _id;
	private String _data;
	private String _name;
	private String _duration;
	private long _albumId;
    private String _album;
    private long _artistId;
    private String _artist;
    private String _title;
	
	public Song() {}
//	public Song(long id, String data, String name, String duration, long album) {
//		this._id = id;
//		this._data = data;
//		this._name = name;
//		this._duration = StringUtils.convertMsToTimeFormat(duration);
//		this._albumId = album;
//	}
	
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
	public long getAlbumId() {
		return _albumId;
	}
	public void setAlbumId(long _album_id) {
		this._albumId = _album_id;
	}
    public String getAlbum() {
        return _album;
    }

    public void setAlbum(String _album) {
        this._album = _album;
    }

    public long getArtistId() {
        return _artistId;
    }

    public void setArtistId(long _artistId) {
        this._artistId = _artistId;
    }

    public String getTitle() {
        return _title;
    }

    public void setTitle(String _title) {
        this._title = _title;
    }

    public String getArtist() {
        return _artist;
    }

    public void setArtist(String _artist) {
        this._artist = _artist;
    }

    public Bitmap getAlbumart(Context context){
	    Bitmap bm = null;
	    try 
	    {
	        final Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");

	        Uri uri = ContentUris.withAppendedId(sArtworkUri, this.getAlbumId());

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

    @Override
    public String toString() {
        return "Song{" +
                "_id=" + _id +
                ", _data='" + _data + '\'' +
                ", _name='" + _name + '\'' +
                ", _duration='" + _duration + '\'' +
                ", _albumId=" + _albumId +
                ", _album='" + _album + '\'' +
                ", _artistId=" + _artistId +
                ", _artist='" + _artist + '\'' +
                ", _title='" + _title + '\'' +
                '}';
    }
}
