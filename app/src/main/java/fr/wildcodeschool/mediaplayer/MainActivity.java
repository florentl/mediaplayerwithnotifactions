package fr.wildcodeschool.mediaplayer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    private static final int NOTIF_PLAYER_ID=1;

    // Audio player
    public static WildPlayer mPlayer = null;
    // Progress bar
    private SeekBar mSeekbar = null;
    // Seekbar update delay
    private static final int SEEKBAR_DELAY = 1000;
    // Thread used to update the seekbar position
    private final Handler mSeekBarHandler = new Handler();
    private Runnable mSeekBarThread;

    private NotificationManagerCompat notificationManager;
    private  RemoteViews playerNotificationView;
    private Notification playerNotification;

    // Application Context is static in order to access it everywhere.
    private static Context appContext;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialization of the application context
        MainActivity.appContext = getApplicationContext();

        // Initialization of the wild audio player
        mPlayer = new WildPlayer(this);
        mPlayer.init(R.string.song, new WildOnPlayerListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mSeekbar.setMax(mp.getDuration());
            }

            @Override
            public void onCompletion(MediaPlayer mp) {
                mSeekBarHandler.removeCallbacks(mSeekBarThread);
                mSeekbar.setProgress(0);
            }
        });

        // Initialization of the seekbar
        mSeekbar = findViewById(R.id.seekBar);
        mSeekbar.setOnSeekBarChangeListener(this);

        // Thread used to update the seekbar position according to the audio player
        mSeekBarThread = new Runnable() {
            @Override
            public void run() {
                // Widget should only be manipulated in UI thread
                mSeekbar.post(() -> mSeekbar.setProgress(mPlayer.getCurrentPosition()));
                // Launch a new request
                mSeekBarHandler.postDelayed(this, SEEKBAR_DELAY);
            }
        };

       //gestion de la notification
        notificationManager = NotificationManagerCompat.from(this);

        playerNotificationView=new RemoteViews(appContext.getPackageName(), R.layout.notif_actions);
        Notification.Builder builder = new Notification.Builder( appContext );
        playerNotification = builder.getNotification();
        playerNotification.contentView=playerNotificationView;
        playerNotification.icon=R.drawable.ic_music;

        Intent playIntent=new Intent(appContext,PlayerNotifReceiver.class);
        playIntent.setAction(PlayerNotifReceiver.PLAY);
        PendingIntent pendingPlayIntent = PendingIntent.getBroadcast(appContext, 0, playIntent, 0);
        playerNotificationView.setOnClickPendingIntent(R.id.playBtn, pendingPlayIntent);

        Intent pauseIntent=new Intent(appContext,PlayerNotifReceiver.class);
        pauseIntent.setAction(PlayerNotifReceiver.PAUSE);
        PendingIntent pendingPauseIntent = PendingIntent.getBroadcast(appContext, 0, pauseIntent, 0);
        playerNotificationView.setOnClickPendingIntent(R.id.pauseBtn, pendingPauseIntent);
    }

    /**
     * OnSeekBarChangeListener interface method implementation
     * @param seekBar Widget related to the event
     * @param progress Current position on the seekbar
     * @param fromUser Define if it is a user action or a programmatic seekTo
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            mPlayer.seekTo(progress);
        }
    }

    /**
     * OnSeekBarChangeListener interface method implementation
     * @param seekBar Widget related to the event
     */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        Log.e("Activity", "onStartTrackingTouch");
        // Stop seekBarUpdate here
        mSeekBarHandler.removeCallbacks(mSeekBarThread);
    }

    /**
     * OnSeekBarChangeListener interface method implementation
     * @param seekBar Widget related to the event
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        Log.e("Activity", "onStopTrackingTouch");
        // Restart seekBarUpdate here
        if (null != mPlayer && mPlayer.isPlaying()) {
            mSeekBarHandler.postDelayed(mSeekBarThread, SEEKBAR_DELAY);
        }
    }

    /**
     * On play button click
     * Launch the playback of the media
     */
    public void playMedia(View v) {
        if (null != mPlayer && mPlayer.play()) {
            mSeekBarHandler.postDelayed(mSeekBarThread, SEEKBAR_DELAY);
        }
    }

    /**
     * On pause button click
     * Pause the playback of the media
     */
    public void pauseMedia(View v) {
        if (null != mPlayer && mPlayer.pause()) {
            mSeekBarHandler.removeCallbacks(mSeekBarThread);
        }
    }

    /**
     * On reset button click
     * Stop the playback of the media
     */
    public void resetMedia(View v) {
        if (null != mPlayer && mPlayer.reset()) {
            mSeekbar.setProgress(0);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        notificationManager.notify( NOTIF_PLAYER_ID,playerNotification );
    }

    @Override
    protected void onResume() {
        super.onResume();
        notificationManager.cancel( NOTIF_PLAYER_ID );

    }

    /**
     * Application context accessor
     * https://possiblemobile.com/2013/06/context/
     * @return The application context
     */
    public static Context getAppContext() {
        return appContext;
    }
}
