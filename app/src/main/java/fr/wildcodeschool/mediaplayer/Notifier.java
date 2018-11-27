package fr.wildcodeschool.mediaplayer;

import android.app.Notification;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;

public class Notifier extends Builder {
    private String title;
    private String artist;

    public Notifier(@NonNull Context context, @NonNull String channelId, String title, String artist) {
        super(context, channelId);
        this.title = title;
        this.artist = artist;
    }

    public Builder setup()
    {
        return this.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setSmallIcon(R.drawable.ic_music)
            // Add media control buttons that invoke intents in your media service
            .addAction(R.drawable.ic_play, "Play", null) // #0
            .addAction(R.drawable.ic_pause, "Pause", null)  // #1
            .addAction(R.drawable.ic_stop, "Stop", null)     // #2
            // Apply the media style template
            .setContentTitle(this.title)
            .setContentText(this.artist);

    }
}
