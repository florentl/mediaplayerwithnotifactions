package fr.wildcodeschool.mediaplayer;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;

public class Notifier extends Builder {
    private String title;
    private String artist;
    private PendingIntent playIntent;
    private PendingIntent pauseIntent;

    public Notifier(@NonNull Context context, @NonNull String channelId, String title, String artist, PendingIntent _playIntent
            , PendingIntent _pauseIntent) {
        super(context, channelId);
        this.title = title;
        this.artist = artist;
        playIntent = _playIntent;
        pauseIntent = _pauseIntent;
    }

    public Builder setup()
    {

        return this.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setSmallIcon(R.drawable.ic_music)
            // Add media control buttons that invoke intents in your media service
            .addAction(R.drawable.ic_play, "Play", playIntent) // #0
            .addAction(R.drawable.ic_pause, "Pause", pauseIntent)  // #1
            //.addAction(R.drawable.ic_stop, "Stop", null)     // #2
            // Apply the media style template
            .setContentTitle(this.title)
            .setContentText(this.artist);

    }
}
