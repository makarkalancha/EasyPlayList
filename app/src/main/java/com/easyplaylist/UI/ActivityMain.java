package com.easyplaylist.UI;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.easyplaylist.data.Song;
import com.easyplaylist.engine.App;
import com.easyplaylist.engine.R;
import com.easyplaylist.utils.StringUtils;

public class ActivityMain extends Activity{
//	private TextView text;
	private ListView list_v;
    private SeekBar seekBar;
    private ImageButton playButton;
    private ImageButton nextButton;
    private ImageButton prevButton;
    private ImageButton pauseButton;
    private TextView songName;
    private TextView startTimeField;
    private TextView endTimeField;

    private List<Song> songs = new ArrayList<Song>();
    private int oneTimeOnly = 0;

    private App appInst;

    private MediaPlayer player;
    private double startTime = 0;
    private double endTime = 0;
    private Handler handler = new Handler();
    private long forwardTime = TimeUnit.SECONDS.toMillis(5);
    private long backwardTime = TimeUnit.SECONDS.toMillis(5);

    private Runnable UpdateSongTime = new Runnable() {
        @Override
        public void run() {
            startTime = player.getCurrentPosition();
            startTimeField.setText(StringUtils.convertMsToTimeFormat(Double.toString(startTime)));
            seekBar.setProgress((int)startTime);
            handler.postDelayed(this, 100);
        }
    };
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
//        text = (TextView) findViewById(R.id.text);
        list_v = (ListView) findViewById(R.id.list_v);
        seekBar = (SeekBar) findViewById(R.id.seek_bar);
        playButton = (ImageButton) findViewById(R.id.play);
        nextButton = (ImageButton) findViewById(R.id.forward);
        prevButton = (ImageButton) findViewById(R.id.rewind);
        pauseButton = (ImageButton) findViewById(R.id.pause);
        songName = (TextView) findViewById(R.id.song_name);
        startTimeField = (TextView) findViewById(R.id.start_time);
        endTimeField = (TextView) findViewById(R.id.end_time);

        seekBar.setClickable(true);
        pauseButton.setEnabled(false);

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
//                MediaStore.Audio.Media._ID);
                MediaStore.Audio.Media.DATA);

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
				Song s = songs.get(position);
				Toast.makeText(appInst,
					      "Click ListItem path " + s.getData()+"; duration:"+s.getDuration(), Toast.LENGTH_LONG)
					      .show();
//                if(player != null){
//                    player.reset();
//                }
//
//                player = MediaPlayer.create(ActivityMain.this, Uri.parse(s.getData()));
//                player.start();
                playSong(s);
//				final Song item = (Song)parent.getItemAtPosition(position);
//				view.animate()
//				.setDuration(2000)
//				.alpha(0)
//				.withEndAction(new Runnable() {
//					@Override
//					public void run() {
//						songs.remove(position);
//						adapter.notifyDataSetChanged();
//						view.setAlpha(1);
//					}
//				});


			}
        	
		});
	}
    public void play(View view){
        if(player != null){
            player.start();
        }else if(songs.size() > 0){
            playSong(songs.get(0));
        }
        Toast.makeText(appInst, "No songs",Toast.LENGTH_LONG).show();
    }

    public void playSong(Song song){
        Toast.makeText(appInst, "Playing song",Toast.LENGTH_LONG).show();
        if(player != null){
            player.reset();
        }
        player = MediaPlayer.create(ActivityMain.this, Uri.parse(song.getData()));
        player.start();
        songName.setText(song.getData());
        startTime = player.getCurrentPosition();
        endTime = player.getDuration();
        if(oneTimeOnly == 0){
            seekBar.setMax((int)endTime);
//            oneTimeOnly = 1;
        }

        startTimeField.setText(StringUtils.convertMsToTimeFormat(Double.toString(startTime)));
        endTimeField.setText(StringUtils.convertMsToTimeFormat(Double.toString(endTime)));
        seekBar.setProgress((int)startTime);
        handler.postDelayed(UpdateSongTime,100);
        pauseButton.setEnabled(true);
        playButton.setEnabled(false);
    }

    public void pause(View view){
        Toast.makeText(appInst, "Pausing song",Toast.LENGTH_LONG).show();
        player.pause();
        pauseButton.setEnabled(false);
        playButton.setEnabled(true);
    }

    public void forward(View view){

    }

    public void rewind(View view){

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
}
