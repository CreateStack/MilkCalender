package com.monday2105.milkcalender;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

public class NotificationHelper {

    private String CHANNEL_ID;
    private Context context;
    private String title;
    private String text;
    private String channel_description;
    private static int notificationId;

    NotificationHelper(Context context, String CHANNEL_ID, String title, String text,
                         String channel_description, int notificationId){
        this.context = context;
        this.CHANNEL_ID = CHANNEL_ID;
        this.title = title;
        this.text = text;
        this.channel_description = channel_description;
        NotificationHelper.notificationId = notificationId;

    }

    public NotificationCompat.Builder createNotification(boolean onGoing) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.ic_stat_name))
                .setColor(ContextCompat.getColor(context,R.color.colorNotif))
                .setContentTitle(title)
                .setContentText(text)
                .setOngoing(onGoing)
                .setOnlyAlertOnce(onGoing)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(notificationId, builder.build());

        return builder;
    }

    void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = CHANNEL_ID;
            String description = channel_description;
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void setIntent(NotificationCompat.Builder builder){
        Intent myIntent = new Intent(context, MainActivity.class);
        myIntent.putExtra("fromNotification", true);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, myIntent, Intent.FILL_IN_ACTION);
        builder.setContentIntent(pendingIntent);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(notificationId, builder.build());

    }

}
