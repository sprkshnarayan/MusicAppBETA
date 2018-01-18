
package com.example.surajprakash.musicapp_beta;

import android.app.Activity;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class ActivityDisplaySongs extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private Button mBtnImport;
    private ListView mListSongs;
    private LinearLayout mLinearListImportedFiles;
    private RelativeLayout mRelativeBtnImport;
    private SongListAdapter mAdapterListFile;
    private String[] STAR = {"*"};
    private ArrayList<Song> mSongList;
    private MusicService serviceMusic;
    private Intent playIntent;
    private EditText eeditText;
    public static int shuffle_status=0;
    public static int loop_status=0;
    public static int imported=0;
    String check;
    String songName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

      //  this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_activity_display_songs);
      //  setSupportActionBar(Toolbar);
        init();
     /*   mBtnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(ActivityDisplaySongs.this, About_Activity.class);
                  startActivity(myIntent);

                //  startActivity(new Intent(this,About_Activity.class));       //  to goto next activity(about activity)

            }
        });*/
        eeditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override                                                                                     // for search option
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                songName = editable.toString();
                mSongList=llistAllSongs();
                mAdapterListFile.setSongList(mSongList);
               // mLinearListImportedFiles.setVisibility(View.VISIBLE);
               // mRelativeBtnImport.setVisibility(View.GONE);
                serviceMusic.clearSongList();
                serviceMusic.setSongList(mSongList);
            }
        });




    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity_main_actions,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId())
        {


            case R.id.sortBySongName_id:
                if(imported==1) {
                    Toast.makeText(getApplicationContext(), "sorted by song name", Toast.LENGTH_LONG).show();
                    mSongList = listAllSongs();
                    Collections.sort(mSongList, Song.Song1comparator);

                    mAdapterListFile.setSongList(mSongList);
                    //   mLinearListImportedFiles.setVisibility(View.VISIBLE);
                    //  mRelativeBtnImport.setVisibility(View.GONE);
                    serviceMusic.clearSongList();
                    serviceMusic.setSongList(mSongList);

                    return true;
                }
                else {
                    Toast.makeText(getApplicationContext(), "To enable, click on import button", Toast.LENGTH_LONG).show();
                    return true;
                }

            case R.id.sortByDuration_id:
                if(imported==1) {
                    Toast.makeText(getApplicationContext(), "Sorted by duration", Toast.LENGTH_LONG).show();
                    mSongList = listAllSongs();
                    Collections.sort(mSongList, Song.Song3comparator);

                    mAdapterListFile.setSongList(mSongList);
                    //  mLinearListImportedFiles.setVisibility(View.VISIBLE);
                    // mRelativeBtnImport.setVisibility(View.GONE);
                    serviceMusic.clearSongList();
                    serviceMusic.setSongList(mSongList);
                    return true;
                }
                else {
                    Toast.makeText(getApplicationContext(), "To enable, click on import button", Toast.LENGTH_LONG).show();
                    return true;
                }


            case R.id.sortByArtist_id:
                if(imported==1) {
                    Toast.makeText(getApplicationContext(), "Sorted by artist ", Toast.LENGTH_LONG).show();
                    mSongList = listAllSongs();
                    Collections.sort(mSongList, Song.Song3comparatorArtist);

                    mAdapterListFile.setSongList(mSongList);
                    //  mLinearListImportedFiles.setVisibility(View.VISIBLE);
                    // mRelativeBtnImport.setVisibility(View.GONE);
                    serviceMusic.clearSongList();
                    serviceMusic.setSongList(mSongList);
                    return true;
                }
                else {
                    Toast.makeText(getApplicationContext(), "To enable, click on import button", Toast.LENGTH_LONG).show();
                    return true;
                }

            case R.id.sortByAlbum_id:
                if (imported==1) {

                    Toast.makeText(getApplicationContext(), "Sorted by Album", Toast.LENGTH_LONG).show();
                    mSongList = listAllSongs();
                    Collections.sort(mSongList, Song.Song3comparatorAlbum);

                    mAdapterListFile.setSongList(mSongList);
                    //  mLinearListImportedFiles.setVisibility(View.VISIBLE);
                    // mRelativeBtnImport.setVisibility(View.GONE);
                    serviceMusic.clearSongList();
                    serviceMusic.setSongList(mSongList);
                    return true;
                }
                else {
                    Toast.makeText(getApplicationContext(), "To enable, click on import button", Toast.LENGTH_LONG).show();
                    return true;
                }

            case R.id.search_id:
                if(imported==1) {


                    Toast.makeText(getApplicationContext(), "search a song ", Toast.LENGTH_LONG).show();
                    eeditText.setVisibility(View.VISIBLE);
                    return true;
                }
                else {
                    Toast.makeText(getApplicationContext(), "To enable, click on import button", Toast.LENGTH_LONG).show();
                    return true;
                }

           // case R.id.About_id:
               // Intent Intent = new Intent(ActivityDisplaySongs.this, AboutActivity.class);
                //startActivity(Intent);

              //  return true;
            case R.id.random_id:
                if (imported==1) {
                    shuffle_status++;
                    shuffle_status = shuffle_status % 2;
                    // String st=Integer.toString(shuffle_status);
                    if (shuffle_status == 1)
                        Toast.makeText(getApplicationContext(), "Shuffle on", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getApplicationContext(), "Shuffle off", Toast.LENGTH_LONG).show();
                    return true;
                }
                else {
                    Toast.makeText(getApplicationContext(), "To enable, click on import button", Toast.LENGTH_LONG).show();
                    return true;
                }


            case R.id.loop_id:
                if(imported==1) {
                    loop_status++;
                    loop_status %= 2;

                    if (loop_status == 1)
                        Toast.makeText(getApplicationContext(), "repeat on", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getApplicationContext(), "repeat off", Toast.LENGTH_LONG).show();

                    return true;
                }
                else {
                    Toast.makeText(getApplicationContext(), "To enable, click on import button", Toast.LENGTH_LONG).show();
                    return true;
                }




        }
        return super.onOptionsItemSelected(item);
    }

   private void init() {

       getActionBar();
       mBtnImport = (Button) findViewById(R.id.btn_import_files);
     //  mBtnAbout = (Button) findViewById(R.id.about);
       mLinearListImportedFiles = (LinearLayout) findViewById(R.id.linear_list_imported_files);
       mRelativeBtnImport = (RelativeLayout) findViewById(R.id.relative_btn_import);
       mListSongs = (ListView) findViewById(R.id.list_songs_actimport);
       eeditText = (EditText)findViewById(R.id.editText);



       mBtnImport.setOnClickListener(this);

       mListSongs.setOnItemClickListener(this);

       mSongList = new ArrayList<Song>();
       mAdapterListFile = new SongListAdapter(ActivityDisplaySongs.this, mSongList);
       mListSongs.setAdapter(mAdapterListFile);
   }
    private ServiceConnection musicConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.PlayerBinder binder = (MusicService.PlayerBinder)service;
            //get service
            serviceMusic = binder.getService();
            serviceMusic.setSongList(mSongList);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    public void onClick(View view) {
        imported=1;

        getSupportActionBar().setTitle("MusicApp_beta");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorRed)));
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){

            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorlessred));
        }



        mSongList = listAllSongs();
        mAdapterListFile.setSongList(mSongList);
        mLinearListImportedFiles.setVisibility(View.VISIBLE);
        mRelativeBtnImport.setVisibility(View.GONE);
        eeditText.setVisibility(View.GONE);
        serviceMusic.setSongList(mSongList);

    }


    private ArrayList<Song> listAllSongs(){
        Cursor cursor;
        ArrayList<Song> songList = new ArrayList<>();
        Uri allSongsUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        if(isSdPresent()){
            cursor = getContentResolver().query(allSongsUri, STAR, selection, null, null);
            if(cursor != null){
                if(cursor.moveToFirst()){
                    do{
                        Song song = new Song();

                        String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                        String[] res = data.split("\\.");
                        song.setSongName(res[0]);

                       // song.setSongAlbumArt(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART)));
                       // song.setBm(BitmapFactory.decodeFile(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART))));

                        check = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                        if(check.isEmpty()){

                            song.setmSongAlbumArtist("<Unknown>");
                        }
                        else {
                            song.setmSongAlbumArtist(check);
                        }


                        song.setSongId(cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)));
                        song.setSongFullPath(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
                        song.setSongAlbumName(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
                        song.setSongUri(ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID))));
                        String duration = getDuration(Integer.parseInt(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))));
                        song.setSongDuration(duration);
                        songList.add(song);


                    }while (cursor.moveToNext());
                    return songList;
                }
                cursor.close();
            }

        }
        return null;

    }


    private ArrayList<Song> llistAllSongs(){
        Cursor cursor;
        ArrayList<Song> songList = new ArrayList<>();
        Uri allSongsUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        if(isSdPresent()){
            cursor = managedQuery(allSongsUri,STAR,selection,null,null);
            if(cursor != null){
                if(cursor.moveToFirst()){
                    do{
                        Song song = new Song();

                        String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                        String[] res = data.split("\\.");
                        if(res[0].toLowerCase().contains(songName.toLowerCase())) {

                            song.setSongName(res[0]);

                            // song.setSongAlbumArt(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART)));
                         //  song.setBm(BitmapFactory.decodeFile(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART))));


                            check = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                            if(check.isEmpty()){

                                song.setmSongAlbumArtist("<Unknown>");
                            }
                            else {
                                song.setmSongAlbumArtist(check);
                            }
                            song.setSongId(cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)));
                            song.setSongFullPath(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
                            song.setSongAlbumName(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
                            song.setSongUri(ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID))));
                            String duration = getDuration(Integer.parseInt(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))));
                            song.setSongDuration(duration);
                            songList.add(song);
                        }


                    }while (cursor.moveToNext());
                    return songList;
                }
                cursor.close();
            }

        }
        return null;

    }




    private static boolean isSdPresent(){
      return android.os.Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
  }


    private static String getDuration(long millis){
        if(millis<0){
            throw new IllegalArgumentException("duration must be greater than zero");
        }

        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        StringBuilder sb = new StringBuilder(6);
        sb.append(minutes < 10 ? "0"+minutes:minutes);
        sb.append(":");
        sb.append(seconds < 10 ? "0"+ seconds : seconds);

        return  sb.toString();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long ld) {

        serviceMusic.setSelectedSong(position,  MusicService.NOTIFICATION_ID);

    }

    @Override
    protected void onStart(){
        super.onStart();
        //start service
        if(playIntent == null){
            playIntent = new Intent(this,MusicService.class);
            bindService(playIntent,musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }


    @Override
    protected void onDestroy(){
        //stopservices
        stopService(playIntent);
        serviceMusic = null;
        super.onDestroy();
    }


}
