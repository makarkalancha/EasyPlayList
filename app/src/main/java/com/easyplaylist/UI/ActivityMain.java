package com.easyplaylist.UI;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
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

import com.easyplaylist.broadcastreceiver.HeadphoneUplugged;
import com.easyplaylist.dao.Song;
import com.easyplaylist.engine.App;
import com.easyplaylist.engine.Player;
import com.easyplaylist.engine.R;
import com.easyplaylist.interfaces.IUpdateUI;
import com.easyplaylist.listeners.IncomingCallListener;
import com.easyplaylist.services.EasyPlaylistService;
import com.easyplaylist.services.EasyPlaylistService.EasyPlaylistBinder;
import com.easyplaylist.utils.StringUtils;

public class ActivityMain extends Activity{
//    private Player _player;

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
    private int oneTimeOnly = 0;
    private AdapterPlaylistItem adapter;
    private int _firstVisibleItem = -1;
    private int _visibleItemCount = -1;

    private HeadphoneUplugged _headphoneUpluggedBroadcast = new HeadphoneUplugged(new ImplUpdateUI());
    private TelephonyManager _tm;
    private IncomingCallListener incomingCallListener = new IncomingCallListener(new ImplUpdateUI());

    private App appInst;

    private double startTime = 0;
    private double endTime = 0;
    private Handler _handler = new Handler();
    private static final int SEEK_STEP = 1000;

    private EasyPlaylistService _service;
    private Intent playIntent;
    private boolean musicBound = false;
    //connect to the service
    private ServiceConnection easyplaylistConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            EasyPlaylistBinder binder = (EasyPlaylistBinder) service;
            //get service
            _service = binder.getService();
            _service.setList(songs);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    private Runnable UpdateSongTime = new Runnable() {
        @Override
        public void run() {
            startTime = _player.getCurrentPosition();
            startTimeField.setText(StringUtils.convertMsToTimeFormat(Double.toString(startTime)));
            seekBar.setProgress((int) startTime);
            _handler.postDelayed(this, SEEK_STEP);
//            Log.i(appInst.LOG_TAG,"in runnable: start="+startTime);
        }
    };
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appInst = (App) getApplication();
        _player = appInst._player;

        setContentView(R.layout.activity_main);

        ll = (LinearLayout) findViewById(R.id.linear_main);
        
        list_v = (ListView) findViewById(R.id.list_v);
        seekBar = (SeekBar) findViewById(R.id.seek_bar);
        playButton = (ImageButton) findViewById(R.id.play_pause);
        nextButton = (ImageButton) findViewById(R.id.next);
        prevButton = (ImageButton) findViewById(R.id.previous);
        songName = (TextView) findViewById(R.id.song_name);
        _tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        _tm.listen(incomingCallListener, PhoneStateListener.LISTEN_CALL_STATE);
        songName.setSelected(true);

        startTimeField = (TextView) findViewById(R.id.start_time);
        endTimeField = (TextView) findViewById(R.id.end_time);

        seekBar.setClickable(true);

        String[] proj = new String[] {
        		MediaStore.Audio.Media._ID,
        		MediaStore.Audio.Media.DATA,
        		MediaStore.Audio.Media.DISPLAY_NAME,
        		MediaStore.Audio.Media.DURATION,
        		MediaStore.Audio.Media.ALBUM_ID
        		
        };
        String where = MediaStore.Audio.Media.MIME_TYPE  + "= 'audio/mpeg'" + " AND "+
        		MediaStore.Audio.Media.DATA +" LIKE ?";
        String[] whereArgs = new String[] {appInst.MUSIC_PATH+"%"};
        Cursor curs = appInst.getContentResolver().query(
        		MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                proj,
                where,
                whereArgs, 
                MediaStore.Audio.Media.DATA);

        if (curs == null) {
            Toast.makeText(getApplicationContext(), "query failed, handle error", Toast.LENGTH_LONG).show();
        } else if (!curs.moveToFirst()) {
        	Toast.makeText(getApplicationContext(), "no media on the device", Toast.LENGTH_LONG).show();
        } else {
        	do {
        		Song s = new Song(curs.getLong(0),curs.getString(1),curs.getString(2),curs.getString(3),curs.getLong(4));
        		songs.add(s);
        		Log.i(appInst.LOG_TAG+"song"+s.getId(),"data:"+s.getData()+";name:"+s.getName()+";duration:"+s.getDuration());
        	}while(curs.moveToNext());
        }

        _player.setSongsList(songs);
        
        Log.i(appInst.LOG_TAG+"info","size:"+songs.size());
        adapter = new AdapterPlaylistItem(this, R.layout.adapter_song, songs);
        list_v.setAdapter(adapter);
        list_v.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                Song s = songs.get(position);
                _player.setCurrentlyPlayingIndex(position);
                adapter.notifyDataSetChanged();
                list_v.setSelection(getCentralPosition());
                play(s);
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
                _player.forward(ActivityMain.this);
                updateUI();
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _player.rewind(ActivityMain.this);
                updateUI();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean isTouched) {
//                Log.i(appInst.LOG_TAG, "seekbar: i=" + progress+"; b="+isTouched);
                if(isTouched) {
                    _player.seekTo(progress);
                    startTime = _player.getCurrentPosition();
                    startTimeField.setText(StringUtils.convertMsToTimeFormat(Double.toString(startTime)));
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
    }

    public void playPause(){
        if(_player.getCurrentlyPlayingIndex() > -1){
            if(_player.isPlaying()){
                _player.pause();
                _handler.removeCallbacks(UpdateSongTime);
                playButton.setImageResource(R.drawable.ic_action_play);
            }else{
                _player.start();
                _handler.postDelayed(UpdateSongTime, SEEK_STEP);
                playButton.setImageResource(R.drawable.ic_action_pause);
            }
        }else if(songs.size() > 0){
            play(songs.get(0));
            _player.setCurrentlyPlayingIndex(0);
            updateUI();
        }
    }

    public void updateUI(){
        songName.setText(_player.getCurrentSong().getData());
        adapter.notifyDataSetChanged();
        list_v.setSelection(getCentralPosition());
        startTime = _player.getCurrentPosition();
        endTime = _player.getDuration();
        if(oneTimeOnly == 0){
            Log.i(appInst.LOG_TAG, "updateUI seekbar: duration=" + endTime);
            seekBar.setMax((int)endTime);
        }
        startTimeField.setText(StringUtils.convertMsToTimeFormat(Double.toString(startTime)));
        endTimeField.setText(StringUtils.convertMsToTimeFormat(Double.toString(endTime)));
        playButton.setImageResource(R.drawable.ic_action_pause);
//        Log.i(App.LOG_TAG, "start:" + startTime + "; end:" + endTime);
        seekBar.setProgress((int) startTime);
        _handler.postDelayed(UpdateSongTime, SEEK_STEP);
    }

    public void play(Song song){
        _player.withOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                _player.forward(ActivityMain.this);
                updateUI();
                Log.i(App.LOG_TAG, "onCompletion Player.play");
            }
        });
        _player.playSong(ActivityMain.this, song);
    }

    @Override
    protected void onDestroy() {
        _handler.removeCallbacks(UpdateSongTime);
        _player.reset();
        _player.release();
        unregisterReceiver(_headphoneUpluggedBroadcast);
        _tm.listen(incomingCallListener, PhoneStateListener.LISTEN_NONE);
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(playIntent == null) {
            playIntent = new Intent(this, EasyPlaylistService.class);
            bindService(playIntent, easyplaylistConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        registerReceiver(_headphoneUpluggedBroadcast, filter);
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    class ImplUpdateUI implements IUpdateUI{
        @Override
        public void update() {
            playButton.setImageResource(R.drawable.ic_action_play);
        }
    }
}
