package com.example.surajprakash.musicapp_beta;

import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import java.util.Comparator;

/**
 * Created by SURAJ PRAKASH on 01-03-2017.
 */
public class Song {

   private String mSongName, mSongAlbumName, mSongFullPath, mSongDuration,mSongAlbumArtist,malbumArtPath;
   // private Bitmap bm;
   // private ImageView iv;
    private Uri mSongUri;
    private int mSongId;

    public Song(){}
    public Song(String name,int id,String album_name,String full_path, String duration,Uri songuri,String albumArtPath){
        this.mSongName = name;
        this.mSongId = id;
        this.mSongAlbumName = album_name;
        this.mSongFullPath = full_path;
        this.mSongDuration = duration;
        this.mSongUri = songuri;
       // this.malbumArtPath = albumArtPath;
    }
 //   public Bitmap getBm(){return bm;}

  //  public void setBm(Bitmap bbm){bm = bbm;}

    public String getSongAlbumArt(){return malbumArtPath;}

    public void setSongAlbumArt(String art_Path){this.malbumArtPath = art_Path; }

    public String getSongName(){return mSongName;}

    public String getmSongAlbumArtist(){return mSongAlbumArtist;}

    public void setmSongAlbumArtist(String mSongAlbumArtistt){this.mSongAlbumArtist = mSongAlbumArtistt;}

    public void setSongName(String mSongName){this.mSongName = mSongName;}

    public String getSongFullPath(){return mSongFullPath;}

    public void setSongFullPath(String mSongFullPath){this.mSongFullPath = mSongFullPath;}

    public String getSongAlbumName(){return mSongAlbumName;}

    public void setSongAlbumName(String mSongAlbumName){this.mSongAlbumName = mSongAlbumName;}

    public String getSongDuration(){return mSongDuration;}

    public void setSongDuration(String mSongDuration){this.mSongDuration = mSongDuration;}

    public int getSongId(){return mSongId;}

    public void setSongId(int mSongId){this.mSongId = mSongId;}

    public void setSongUri(Uri uri){this.mSongUri = uri;}

    public Uri getSongUri(){return this.mSongUri;}

    public static Comparator<Song> Song1comparator = new Comparator<Song>() {
        @Override
        public int compare(Song s1, Song s2) {
            String songName1 = s1.getSongName().toLowerCase();
            String songName2 = s2.getSongName().toLowerCase();
            //ascending order


            return songName1.compareTo(songName2);
        }
    };




    public static Comparator<Song> Song3comparator = new Comparator<Song>() {
        @Override
        public int compare(Song s1, Song s2) {
            String songduration1 = s1.getSongDuration().toLowerCase();
            String songduration2 = s2.getSongDuration().toLowerCase();
            //ascending order


            return songduration1.compareTo(songduration2);
        }
    };

    public static Comparator<Song> Song3comparatorAlbum = new Comparator<Song>() {
        @Override
        public int compare(Song s1, Song s2) {
            String songduration1 = s1.getSongAlbumName().toLowerCase();
            String songduration2 = s2.getSongAlbumName().toLowerCase();
            //ascending order


            return songduration1.compareTo(songduration2);
        }
    };


    public static Comparator<Song> Song3comparatorArtist = new Comparator<Song>() {
        @Override
        public int compare(Song s1, Song s2) {
            String songduration1 = s1.getmSongAlbumArtist().toLowerCase();
            String songduration2 = s2.getmSongAlbumArtist().toLowerCase();
            //ascending order


            return songduration1.compareTo(songduration2);
        }
    };







}
