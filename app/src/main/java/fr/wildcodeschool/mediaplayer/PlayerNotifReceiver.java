package fr.wildcodeschool.mediaplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class PlayerNotifReceiver extends BroadcastReceiver {

    public static final String PLAY = "play";
    public static final String PAUSE = "pause";

    @Override
    public void onReceive(Context context, Intent intent) {

        String intentAction = intent.getAction();
        switch (intentAction) {
            case PLAY:
                MainActivity.mPlayer.play();
                break;
            case PAUSE:
                MainActivity.mPlayer.pause();
                break;
        }
    }


}
