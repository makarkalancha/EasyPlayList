package com.easyplaylist.UI;

import java.util.ArrayList;
import java.util.List;

import com.easyplaylist.dao.Song;
import com.easyplaylist.engine.App;
import com.easyplaylist.engine.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterPlaylistItem extends ArrayAdapter<Song>{
//	private Map<String, Integer> mIdMap = new HashMap<String, Integer>();
	private List<Song> _songs = new ArrayList<Song>();
    private final Activity _context;
//    private int current_position = -1;
//    private App appInst;

    public AdapterPlaylistItem(Activity context, int textViewResourceId, List<Song> songs) {
        super(context, textViewResourceId, songs);
    	this._context = context;
    	this._songs = songs;
//    	for (int i = 0; i < songs.size(); ++i) {
//    		mIdMap.put(songs.get(i).getName(), i);
//    	}
//        appInst =  (App) getContext();
    }

//    @Override
//    public long getItemId(int position) {
//      Song item = getItem(position);
//      return mIdMap.get(item);
//    }
//
//    @Override
//    public boolean hasStableIds() {
//      return true;
//    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = _context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.adapter_song, null);
            ViewHolder viewHolder = new ViewHolder();
            //        viewHolder.text = (TextView) rowView.findViewById(R.id.firstLine);
            viewHolder.text1 = (TextView) rowView.findViewById(R.id.firstLine);
            viewHolder.text2 = (TextView) rowView.findViewById(R.id.secondLine);
            viewHolder.image = (ImageView) rowView.findViewById(R.id.icon);
            rowView.setTag(viewHolder);


        }

        ViewHolder holder = (ViewHolder) rowView.getTag();
        Song s = _songs.get(position);

//        Log.i(App.LOG_TAG+"adapter3", "cur_pos: " + App.currentlyPlayingIndex+"; pos: "+position+"; equal: "+(App.currentlyPlayingIndex == position));
        if(App._player.getCurrentlyPlayingIndex() == position){
            rowView.setBackgroundResource(R.color.list_adapter_active_color);
//            Log.i(App.LOG_TAG+"adapter3", "cur_pos: " + App.currentlyPlayingIndex+"; pos: "+position+"; equal: "+(App.currentlyPlayingIndex == position));
        }else{
            rowView.setBackgroundResource(R.color.list_adapter_def_color);
        }


      
//      MediaMetadataRetriever mediaMetadataRetriever = (MediaMetadataRetriever) new MediaMetadataRetriever();
//      Uri uri = Uri.parse(s.getData());
//      mediaMetadataRetriever.setDataSource(_context, uri);
//      String title = (String) mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        Bitmap bitmap = s.getAlbumart(_context);
        if(bitmap == null) {
            bitmap = BitmapFactory.decodeResource(_context.getResources(),R.drawable.logo);
        }
        holder.image.setImageBitmap(bitmap);

//        if(rowView.isSelected()){
//            Log.i(App.LOG_TAG+"adapter2", "cur_pos" + position);
//        }




        holder.text1.setText(s.getName());
        holder.text2.setText(s.getDuration());
        //      if (s.startsWith("Windows7") || s.startsWith("iPhone")
        //          || s.startsWith("Solaris")) {
        //        holder.image.setImageResource(R.drawable.no);
        //      } else {
        //        holder.image.setImageResource(R.drawable.ok);
        //      }

        return rowView;
    }
    
    static class ViewHolder {
    	public TextView text1;
    	public TextView text2;
        public ImageView image;
    }
}
