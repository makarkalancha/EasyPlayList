package com.easyplaylist.engine;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.easyplaylist.data.Song;

public class ActivityMain extends Activity{
//	private TextView text;
	private ListView list_v;
	private App appInst;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
//        text = (TextView) findViewById(R.id.text);
        list_v = (ListView) findViewById(R.id.list_v);
        appInst = (App) getApplicationContext();
//        File dir = new File(appInst.MUSIC_PATH);
//        String[] files = dir.list();
        String textSt = "";
//        for(String file : files){
//        	textSt += (file+"\r\n"); 
//        }
//        text.setText(textSt);
//        text.setText(appInst.MUSIC_PATH);
//        text.setText(FileUtils.walk(appInst.MUSIC_PATH));
//        getContentResolver().query(appInst.MUSIC_PATH, projection, selection, selectionArgs, sortOrder)
        String[] proj = new String[] {
        		MediaStore.Audio.Media._ID,
        		MediaStore.Audio.Media.DATA,
        		MediaStore.Audio.Media.DISPLAY_NAME,
        		MediaStore.Audio.Media.DURATION,
        		MediaStore.Audio.Media.ALBUM_ID
        		
        };
//        Log.i(">>uri",Uri.fromParts("content", appInst.MUSIC_PATH, null).toString());
//        Log.i(">>uri",Uri.parse(appInst.MUSIC_PATH).toString());
//        String where = MediaStore.Audio.Media.MIME_TYPE  + "= 'audio/mpeg'" + " AND "+
//        		MediaStore.Audio.Artists._ID +" IN (" +
//        				"SELECT "+MediaStore.Audio.Media.ARTIST_ID+" FROM AUDIO "+
//        				"WHERE "+MediaStore.Audio.Media.DATA +" LIKE ?" +
//        		")";
        String where = MediaStore.Audio.Media.MIME_TYPE  + "= 'audio/mpeg'" + " AND "+
        		MediaStore.Audio.Media.DATA +" LIKE ?";
        String[] whereArgs = new String[] {appInst.MUSIC_PATH+"%"};
        Cursor curs = appInst.getContentResolver().query(
        		MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
//        		MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
//        		Uri.parse(appInst.MUSIC_PATH),
//        		Uri.fromParts("content", appInst.MUSIC_PATH, null),
                proj,
//        		null,
//                MediaStore.Audio.Media.MIME_TYPE  + "= 'audio/mpeg'",
                where,
                whereArgs, 
                MediaStore.Audio.Media._ID);
        final List<Song> songs = new ArrayList<Song>();
        if (curs == null) {
            Toast.makeText(getApplicationContext(), "query failed, handle error", Toast.LENGTH_LONG).show();
//            Log.e("Cursor", "query failed, handle error");
        } else if (!curs.moveToFirst()) {
        	Toast.makeText(getApplicationContext(), "no media on the device", Toast.LENGTH_LONG).show();
//        	Log.e("Cursor", "no media on the device");
        } else {
        	do {
//        		long id = curs.getLong(0);
//        		String data = curs.getString(1);
//        		String name = curs.getString(2);
//        		String duration = curs.getString(3);
//        		textSt += Long.toString(id)+";"+data+";"+name+";"+duration+"\r\n\r\n";
        		Song s = new Song(curs.getLong(0),curs.getString(1),curs.getString(2),curs.getString(3),curs.getLong(4));
        		songs.add(s);
        		Log.i("song"+s.getId(),"data:"+s.getData()+";name:"+s.getName()+";duration:"+s.getDuration());
        	}while(curs.moveToNext());
        }
        
        Log.i("info","size:"+songs.size());
        
        
//        text.setText(textSt);
//        final AdapterPlaylistItem adapter = new AdapterPlaylistItem(this, R.layout.adapter_song, songs);
        final AdapterPlaylistItem adapter = new AdapterPlaylistItem(this, R.layout.adapter_song, songs);
        list_v.setAdapter(adapter);
        list_v.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
//				Song s = songs.get(position);
//				Toast.makeText(appInst,
//					      "Click ListItem path " + s.getData()+"; duration:"+s.getDuration(), Toast.LENGTH_LONG) 
//					      .show();
				final Song item = (Song)parent.getItemAtPosition(position);
				view.animate()
				.setDuration(2000)
				.alpha(0)
				.withEndAction(new Runnable() {
					@Override
					public void run() {
						songs.remove(position);	
						adapter.notifyDataSetChanged();
						view.setAlpha(1);
					}
				});
			}
        	
		});
        
	}

}
