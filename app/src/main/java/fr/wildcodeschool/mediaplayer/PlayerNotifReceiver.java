package fr.wildcodeschool.mediaplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PlayerNotifReceiver extends BroadcastReceiver {

    private WildPlayer myPlayer;

    public PlayerNotifReceiver() {

    }

    public PlayerNotifReceiver(WildPlayer myPlayer) {
        super();
        this.myPlayer = myPlayer;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if("pause".equals(intent.getAction())) {
            myPlayer.pause();
        } else if("play".equals( intent.getAction() )) {
            myPlayer.play();
        }
    }
}
