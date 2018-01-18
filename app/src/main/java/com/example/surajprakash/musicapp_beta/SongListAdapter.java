package com.example.surajprakash.musicapp_beta;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by SURAJ PRAKASH on 01-03-2017.
 */
public class SongListAdapter extends BaseAdapter{

    private Context mContext;
    private ArrayList<Song> songList; // data Source for list view;

    public SongListAdapter(Context context,ArrayList<Song> list){
        mContext = context;
        this.songList = list;

    }

    @Override
    public int getCount() {
        return songList.size();
    }

    @Override
    public Song getItem(int position) {
        return songList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            //layout inflate for list item
            convertView = LayoutInflater.from(mContext).inflate(R.layout.song_list_item,null);

        }

        ImageView mImgSong = (ImageView)convertView.findViewById(R.id.img_listitem_file);
        TextView mtxtSongName = (TextView)convertView.findViewById(R.id.txt_listitem_filename);
        TextView mTxtSongAlbumName = (TextView) convertView.findViewById(R.id.txt_listitem_albumname);
        TextView mTextSongDuration = (TextView) convertView.findViewById(R.id.txt_listitem_duration);
        TextView mTextSongArtist = (TextView)convertView.findViewById(R.id.artist);

       // Drawable img = Drawable.createFromPath(songList.get(position).getSongAlbumArt());
       // mImgSong.setImageDrawable(img);
      /*
        Bitmap bm= BitmapFactory.decodeFile(getSon)
*/
       // String s1=songList.get(position).getSongAlbumArt();

        //Bitmap bm = BitmapFactory.decodeFile(s1);

       // mImgSong.setImageBitmap(songList.get(position).getBm());
       mImgSong.setImageResource(R.drawable.head);



        mtxtSongName.setText((songList.get(position).getSongName()));
        mTxtSongAlbumName.setText((songList.get(position).getSongAlbumName()));
        mTextSongDuration.setText(songList.get(position).getSongDuration());
        mTextSongArtist.setText((songList.get(position).getmSongAlbumArtist()));

        return convertView;
    }

    public void setSongList(ArrayList<Song> list){
        songList = list;
        this.notifyDataSetChanged();
    }
}
