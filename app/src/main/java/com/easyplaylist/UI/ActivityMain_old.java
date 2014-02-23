package com.easyplaylist.UI;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.easyplaylist.data.Song;
import com.easyplaylist.engine.App;
import com.easyplaylist.engine.Player;
import com.easyplaylist.engine.R;
import com.easyplaylist.utils.StringUtils;

public class ActivityMain_old extends Activity{
    private Player _player;

//	private TextView text;
	private ListView list_v;
    private SeekBar seekBar;
    private ImageButton playButton;
    private ImageButton nextButton;
    private ImageButton prevButton;
    private TextView songName;
    private TextView startTimeField;
    private TextView endTimeField;

    private LinearLayout ll;

    private List<Song> songs = new ArrayList<Song>();
//    private int currentlyPlayingIndex = -1;
    private int oneTimeOnly = 0;
    private AdapterPlaylistItem adapter;
    private int _firstVisibleItem = -1;
    private int _visibleItemCount = -1;

    private App appInst;

//    private MediaPlayer player;
    private double startTime = 0;
    private double endTime = 0;
    private Handler _handler = new Handler();
////    private long forwardTime = TimeUnit.SECONDS.toMillis(5);
////    private long backwardTime = TimeUnit.SECONDS.toMillis(5);
    private static final int SEEK_STEP = 1000;

    private Runnable UpdateSongTime = new Runnable() {
        @Override
        public void run() {
            startTime = _player.getCurrentPosition();
            startTimeField.setText(StringUtils.convertMsToTimeFormat(Double.toString(startTime)));
            seekBar.setProgress((int) startTime);
            _handler.postDelayed(this, SEEK_STEP);
            Log.i(appInst.LOG_TAG,"in runnable: start="+startTime);
        }
    };
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appInst = (App) getApplication();
        _player = appInst._player;

        setContentView(R.layout.activity_main);

        ll = (LinearLayout) findViewById(R.id.linear_main);
        
//        text = (TextView) findViewById(R.id.text);
        list_v = (ListView) findViewById(R.id.list_v);
        seekBar = (SeekBar) findViewById(R.id.seek_bar);
        playButton = (ImageButton) findViewById(R.id.play_pause);
        nextButton = (ImageButton) findViewById(R.id.next);
        prevButton = (ImageButton) findViewById(R.id.previous);
        songName = (TextView) findViewById(R.id.song_name);
        songName.setSelected(true);
//        songName.setEnabled(true);
//        Animation textToLeft = new TranslateAnimation(1000, -1000, 0, 0);
//        textToLeft.setDuration(TimeUnit.SECONDS.toMillis(20));
//        textToLeft.setRepeatCount(Animation.INFINITE);
//        textToLeft.setRepeatMode(Animation.RESTART);
//        songName.setAnimation(textToLeft);


        startTimeField = (TextView) findViewById(R.id.start_time);
        endTimeField = (TextView) findViewById(R.id.end_time);

        seekBar.setClickable(true);

        ////http://stackoverflow.com/questions/5018545/getapplication-vs-getapplicationcontext
        ////no getApplicationContext

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
//        Log.i(appInst.LOG_TAG+"uri",Uri.fromParts("content", appInst.MUSIC_PATH, null).toString());
//        Log.i(appInst.LOG_TAG+"uri",Uri.parse(appInst.MUSIC_PATH).toString());
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
//            Log.e(appInst.LOG_TAG, "query failed, handle error");
        } else if (!curs.moveToFirst()) {
        	Toast.makeText(getApplicationContext(), "no media on the device", Toast.LENGTH_LONG).show();
//        	Log.e(appInst.LOG_TAG, "no media on the device");
        } else {
        	do {
//        		long id = curs.getLong(0);
//        		String data = curs.getString(1);
//        		String name = curs.getString(2);
//        		String duration = curs.getString(3);
//        		textSt += Long.toString(id)+";"+data+";"+name+";"+duration+"\r\n\r\n";
        		Song s = new Song(curs.getLong(0),curs.getString(1),curs.getString(2),curs.getString(3),curs.getLong(4));
        		songs.add(s);
        		Log.i(appInst.LOG_TAG+"song"+s.getId(),"data:"+s.getData()+";name:"+s.getName()+";duration:"+s.getDuration());
        	}while(curs.moveToNext());
        }

        _player.setSongsList(songs);
        
        Log.i(appInst.LOG_TAG+"info","size:"+songs.size());
        
        
//        text.setText(textSt);
//        final AdapterPlaylistItem adapter = new AdapterPlaylistItem(this, R.layout.adapter_song, songs);
        adapter = new AdapterPlaylistItem(this, R.layout.adapter_song, songs);
        list_v.setAdapter(adapter);
        list_v.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
//				view.setSelected(true);
                Song s = songs.get(position);
                _player.setCurrentlyPlayingIndex(position);
                adapter.notifyDataSetChanged();
//                list_v.setSelection(appInst.currentlyPlayingIndex);
                list_v.setSelection(getCentralPosition());

//                view.setBackgroundColor(R.drawable.bg_color);

//				Toast.makeText(appInst,
//					      "Click ListItem path " + s.getData()+"; duration:"+s.getDuration(), Toast.LENGTH_LONG)
//					      .show();
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

        list_v.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                _firstVisibleItem = firstVisibleItem;
                _visibleItemCount = visibleItemCount;
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playPause();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forward();
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rewind();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean isTouched) {
                Log.i(appInst.LOG_TAG, "seekbar: i=" + progress+"; b="+isTouched);
                if(isTouched) {
                    _player.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
	}

    private int getCentralPosition(){
        int center = (int) Math.ceil(_visibleItemCount/2d) - 1;
        int appliedCenter = _player.getCurrentlyPlayingIndex() - center;
        Log.i(appInst.LOG_TAG, "center: "+center+"; appliedCenter:" + appliedCenter);
        return (appliedCenter < 0) ? 0 : appliedCenter;
//        return appliedCenter;
    }

    public void playPause(){
        if(_player != null){
            if(_player.isPlaying()){
                _player.pause();
                playButton.setImageResource(R.drawable.ic_action_play);
            }else{
                _player.start();
                playButton.setImageResource(R.drawable.ic_action_pause);
            }
        }else if(songs.size() > 0){
            playSong(songs.get(0));
            _player.setCurrentlyPlayingIndex(0);
            adapter.notifyDataSetChanged();
//            list_v.setSelection(appInst.currentlyPlayingIndex);
            list_v.setSelection(getCentralPosition());
        }
//        Toast.makeText(appInst, "No songs",Toast.LENGTH_LONG).show();
    }

    public void playSong(Song song){
////        Toast.makeText(appInst, "Playing song",Toast.LENGTH_LONG).show();
//        if(player != null){
//            player.reset();
//        }
//        player = MediaPlayer.create(ActivityMain.this, Uri.parse(song.getData()));
//        player.start();
//
//        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mediaPlayer) {
//                forward();
//                Log.i(appInst.LOG_TAG, "onCompletion");
//            }
//        });

        _player.playSong(ActivityMain_old.this, song);
        playButton.setImageResource(R.drawable.ic_action_pause);
        songName.setText(song.getData());


//        Log.i(appInst.LOG_TAG, "is songName selected:" + songName.isSelected());
//        Log.i(appInst.LOG_TAG, "view focus:" + ll.getFocusedChild());
//        list_v.setSelected(false);
//        View focusedView = ll.getFocusedChild();
//        focusedView.clearFocus();
//        Log.i(appInst.LOG_TAG, "after clear view focus:" + ll.getFocusedChild());


        startTime = _player.getCurrentPosition();
        endTime = _player.getDuration();
        if(oneTimeOnly == 0){
            seekBar.setMax((int)endTime);
//            oneTimeOnly = 1;
        }

        startTimeField.setText(StringUtils.convertMsToTimeFormat(Double.toString(startTime)));
        endTimeField.setText(StringUtils.convertMsToTimeFormat(Double.toString(endTime)));
        seekBar.setProgress((int) startTime);
//        _handler.postDelayed(UpdateSongTime, SEEK_STEP);

//        songName.setSelected(true);
//        songName.setEnabled(true);
//        songName.setFocusableInTouchMode(true);
//        list_v.setSelected(false);
//        songName.setFocusable(true);
//        Log.i(appInst.LOG_TAG, "request focus:" + songName.requestFocus());
//        songName.setSelected(true);
//        Log.i(appInst.LOG_TAG, "after clear view focus:" + ll.getFocusedChild());
//        LinearLayout player = (LinearLayout) findViewById(R.id.player);
//        Log.i(appInst.LOG_TAG, "after clear view focus:" + ll.getFocusedChild());
    }

    public void pause(){
//        Toast.makeText(appInst, "Pausing song",Toast.LENGTH_LONG).show();
        _player.pause();
        _handler.removeCallbacks(UpdateSongTime);
//        _handler.sendEmptyMessageDelayed(0, 0L);
//        songName.setSelected(false);
//        playButton.setEnabled(true);
    }

    public void forward(){
        if(songs.size() > 0){
            if(_player.getCurrentlyPlayingIndex() == songs.size() - 1) {
                _player.setCurrentlyPlayingIndex(0);
            }else {
                int ind = _player.getCurrentlyPlayingIndex();
                _player.setCurrentlyPlayingIndex(ind++);
            }
            adapter.notifyDataSetChanged();
//            list_v.setSelection(appInst.currentlyPlayingIndex);
            list_v.setSelection(getCentralPosition());
            playSong(songs.get(_player.getCurrentlyPlayingIndex()));
        }
    }

    public void rewind(){
        if(songs.size() > 0){
            if(_player.getCurrentlyPlayingIndex() == 0) {
                _player.setCurrentlyPlayingIndex(songs.size() - 1);
            }else{
                int ind = _player.getCurrentlyPlayingIndex();
                _player.setCurrentlyPlayingIndex(ind--);
            }
            adapter.notifyDataSetChanged();
//            list_v.setSelection(appInst.currentlyPlayingIndex);
            list_v.setSelection(getCentralPosition());
            playSong(songs.get(_player.getCurrentlyPlayingIndex()));
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        _player.reset();
        _player.release();
    }

    //TODO
    //other lifecycle methods
}