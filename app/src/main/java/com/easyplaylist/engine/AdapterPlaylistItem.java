package com.easyplaylist.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.easyplaylist.data.Song;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
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

    public AdapterPlaylistItem(Activity context, int textViewResourceId, List<Song> songs) {
    	super(context, textViewResourceId, songs);
    	this._context = context;
    	this._songs = songs;
//    	for (int i = 0; i < songs.size(); ++i) {
//    		mIdMap.put(songs.get(i).getName(), i);
//    	}
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
      
//      MediaMetadataRetriever mediaMetadataRetriever = (MediaMetadataRetriever) new MediaMetadataRetriever();
//      Uri uri = Uri.parse(s.getData());
//      mediaMetadataRetriever.setDataSource(_context, uri);
//      String title = (String) mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
      Bitmap bitmap = s.getAlbumart(_context);
      if(bitmap == null) {
    	  bitmap = BitmapFactory.decodeResource(_context.getResources(),R.drawable.ic_launcher);
      }
      holder.image.setImageBitmap(bitmap);
      
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
