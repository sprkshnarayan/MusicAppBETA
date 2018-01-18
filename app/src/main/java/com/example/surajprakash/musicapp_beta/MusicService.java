package com.example.surajprakash.musicapp_beta;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by SURAJ PRAKASH on 01-03-2017.
 */
public class MusicService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {

    public MediaPlayer mPlayer;
    private Uri mSongUri;

    private ArrayList<Song> mListSongs;
    private int SONG_POS = 0;

    private final IBinder musicBind = new PlayerBinder();

    private final String ACTION_STOP = "com.example.surajprakash.musicapp_beta.STOP";
    private final String ACTION_NEXT = "com.example.surajprakash.musicapp_beta.NEXT";
    private final String ACTION_PREVIOUS = "com.example.surajprakash.musicapp_beta.PREVIOUS";
    private final String ACTION_PAUSE = "com.example.surajprakash.musicapp_beta.PAUSE";


  //  private final String ACTION_RANDOM = "com.example.surajprakash.musicapp_beta.RANDOM";

    private static final int STATE_PAUSED = 1;
    private static final int STATE_PLAYING = 2;
  //  private static final int STATE_RANDOM = 3;


  //  private static  int VAR_RANDOM=0;


    private int mState = 0;
    private static final int REQUEST_CODE_PAUSE = 101;
    private static final int REQUEST_CODE_PREVIOUS = 102;
    private static final int REQUEST_CODE_NEXT = 103;
    private static final int REQUEST_CODE_STOP = 104;

  //  private static final int REQUEST_CODE_RANDOM=105;

    public static int NOTIFICATION_ID = 11;
    private Notification.Builder notificationBuilder;
    private Notification mNotification;


    public class PlayerBinder extends Binder{//service connection to play in background
        public MusicService getService(){
            Log.d("test","getService()");
            return MusicService.this;
        }
    }
    @Override
    public IBinder onBind(Intent intent){
        Log.d("test","onBind called");
        return musicBind;
    }



    @Override
    public void onCreate() {
        super.onCreate();
        //intitializing the media player  object
        mPlayer = new MediaPlayer();
        initPlayer();
        mPlayer.setOnPreparedListener(this);
        mPlayer.setOnCompletionListener(this);
        mPlayer.setOnErrorListener(this);
        notificationBuilder = new Notification.Builder(getApplicationContext());

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {// a service is created
        if(intent != null)

        {
            String action = intent.getAction();
            if(!TextUtils.isEmpty(action)){
                if(action.equals(ACTION_PAUSE)){
                    playPauseSong();
                } else if (action.equals(ACTION_NEXT)){
                    nextSong();
                } else if (action.equals(ACTION_PREVIOUS)){
                    previousSong();
                } else  if (action.equals(ACTION_STOP)){
                    stopSong();
                    stopSelf();
                }
                 /* else if (action.equals(ACTION_RANDOM)){
                    if(ActivityDisplaySongs.shuffle_status==0)ActivityDisplaySongs.shuffle_status=1;
                    else if(ActivityDisplaySongs.shuffle_status==1)ActivityDisplaySongs.shuffle_status=0;
                  */

                //}

            }
        }


        return super.onStartCommand(intent, flags, startId);
    }

    private void initPlayer(){
        mPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }
    public void startSong(Uri songuri, String songName){
        mPlayer.reset();
        mState = STATE_PLAYING;

        mSongUri = songuri;


        try {
            mPlayer.setDataSource(getApplicationContext(),mSongUri);

        } catch (Exception e) {
           Log.e("MUSIC SERVICE","Error setting data source", e);
        }
        mPlayer.prepareAsync();
        mPlayer = MediaPlayer.create(getApplicationContext(),mSongUri);
        mPlayer.start();
        updateNotification(songName);

        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                nextSong();
            }
        });
    }



    public  void playPauseSong(){

        if (mState == STATE_PAUSED){
            mState = STATE_PLAYING;
            mPlayer.start();
        }else {
            mState = STATE_PAUSED;
            mPlayer.pause();
        }

    }

    public void stopSong() {
        mPlayer.stop();
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(NOTIFICATION_ID);

        System.exit(0);

    }

    public void nextSong() {
        // SONG_POS++;


        if (ActivityDisplaySongs.loop_status == 1) {
            startSong(mListSongs.get((SONG_POS) % mListSongs.size()).getSongUri(), mListSongs.get((SONG_POS) % mListSongs.size()).getSongName());
        } else if (ActivityDisplaySongs.shuffle_status == 1) {
            Random random = new Random();
            int rno = 1 + (random.nextInt(mListSongs.size()) % (mListSongs.size()));
            SONG_POS = rno;
            startSong(mListSongs.get(SONG_POS).getSongUri(), mListSongs.get(SONG_POS).getSongName());
        } else {
            startSong(mListSongs.get((SONG_POS + 1) % mListSongs.size()).getSongUri(), mListSongs.get((SONG_POS + 1) % mListSongs.size()).getSongName());
          //  if (ActivityDisplaySongs.loop_status == 0)
                SONG_POS++;
        }
    }

    public void previousSong(){
        //SONG_POS--;


        if (ActivityDisplaySongs.loop_status==1)
        {
            startSong(mListSongs.get(SONG_POS ).getSongUri(), mListSongs.get(SONG_POS ).getSongName());
        }
       else if(ActivityDisplaySongs.shuffle_status==1)
        {
            Random random=new Random();
            int rno=1+(random.nextInt(mListSongs.size())%(mListSongs.size()));

            SONG_POS=rno;
            startSong(mListSongs.get(SONG_POS ).getSongUri(), mListSongs.get(SONG_POS ).getSongName());

        }
      else  if(SONG_POS==0)
        {  SONG_POS=mListSongs.size()-1;
            startSong(mListSongs.get(SONG_POS).getSongUri(),mListSongs.get(SONG_POS).getSongName());
           // SONG_POS--;
        }
        else {
            startSong(mListSongs.get(SONG_POS - 1).getSongUri(), mListSongs.get(SONG_POS - 1).getSongName());
          //  if(ActivityDisplaySongs.loop_status==0)
            SONG_POS--;
        }
    }


    public void setSongURI(Uri uri){this.mSongUri = uri;}

    public void setSelectedSong(int pos, int notification_id){

      /*  if(mPlayer.isPlaying()){
            mPlayer.stop();
            mPlayer.release();
        }*/
        SONG_POS = pos;
        NOTIFICATION_ID = notification_id;
        setSongURI(mListSongs.get(SONG_POS).getSongUri());
        showNotification();
        startSong(mListSongs.get(SONG_POS).getSongUri(),mListSongs.get(SONG_POS).getSongName());


    }
    public void setSongList(ArrayList<Song> listSong){mListSongs = listSong;}
    public void clearSongList(){mListSongs.clear();}

    public void showNotification(){
        PendingIntent pendingIntent;
        Intent intent;


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        RemoteViews notificationView = new RemoteViews(getPackageName(),R.layout.notification_mediacontroller);


        notificationView.setTextViewText(R.id.notify_song_name, mListSongs.get(SONG_POS).getSongName());




        intent = new Intent(ACTION_STOP);
        pendingIntent = PendingIntent.getService(getApplicationContext(),REQUEST_CODE_STOP,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        notificationView.setOnClickPendingIntent(R.id.notify_btn_stop,pendingIntent);

        intent = new Intent(ACTION_PAUSE);
        pendingIntent = PendingIntent.getService(getApplicationContext(),REQUEST_CODE_PAUSE,intent,pendingIntent.FLAG_UPDATE_CURRENT);
        notificationView.setOnClickPendingIntent(R.id.notify_btn_pause,pendingIntent);

        intent = new Intent(ACTION_PREVIOUS);
        pendingIntent = pendingIntent.getService(getApplicationContext(),REQUEST_CODE_PREVIOUS,intent,pendingIntent.FLAG_UPDATE_CURRENT);
        notificationView.setOnClickPendingIntent(R.id.notify_btn_previous, pendingIntent);

        intent = new Intent(ACTION_NEXT);
        pendingIntent = pendingIntent.getService(getApplicationContext(),REQUEST_CODE_NEXT,intent,pendingIntent.FLAG_UPDATE_CURRENT);
        notificationView.setOnClickPendingIntent(R.id.notify_btn_next,pendingIntent);

       /* intent = new Intent(ACTION_RANDOM);
        pendingIntent = pendingIntent.getService(getApplicationContext(),REQUEST_CODE_RANDOM,intent,pendingIntent.FLAG_UPDATE_CURRENT);
        notificationView.setOnClickPendingIntent(R.id.notify_btn_random,pendingIntent);
*/
        mNotification = notificationBuilder
                .setSmallIcon(R.drawable.head).setOngoing(true)
                .setWhen(System.currentTimeMillis())
                .setContent(notificationView)
                .setDefaults(Notification.FLAG_NO_CLEAR)
                .build();
        notificationManager.notify(NOTIFICATION_ID,mNotification);


    }

    private void updateNotification(String songName){
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        mNotification.contentView.setTextViewText(R.id.notify_song_name,songName);
        notificationManager.notify(NOTIFICATION_ID,mNotification);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }



    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {

    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }
}
