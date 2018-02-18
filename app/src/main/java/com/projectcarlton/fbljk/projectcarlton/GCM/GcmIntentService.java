package com.projectcarlton.fbljk.projectcarlton.GCM;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GcmListenerService;
import com.projectcarlton.fbljk.projectcarlton.R;

public class GcmIntentService extends GcmListenerService {

    @Override
    public void onMessageReceived(String from, Bundle data) {
        // First create a NotificationChannel if neither one exists
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        String channelId = "ProjectCarltonChannel";
        if (notificationManager.getNotificationChannel(channelId) == null) {
            CharSequence channelName = "ProjectCarlton Notification";
            String channelDescription = "Notifications";
            int channelImportance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(channelId, channelName, channelImportance);
            channel.setDescription(channelDescription);
            channel.enableLights(true);
            channel.setLightColor(Color.BLUE);
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
        }

        String message = data.getString("message");
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Test")
                .setContentText(message);
        notificationManager.notify(1, mBuilder.build());
    }

}