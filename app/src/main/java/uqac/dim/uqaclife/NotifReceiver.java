package uqac.dim.uqaclife;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import java.util.Random;

public class NotifReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "1234";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent notificationIntent = new Intent(context, NotificationActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(NotificationActivity.class);
        stackBuilder.addNextIntent(notificationIntent);
        Random rn = new Random();
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(100, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(context);


            Notification notification = builder.setContentTitle(intent.getStringExtra("nom"))
                    .setContentText(intent.getStringExtra("room"))
                    .setSmallIcon(R.drawable.mini_ul)
                    .setLargeIcon(Bitmap.createBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_ulrond2)))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID);
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(channel);
        }
            notificationManager.notify(1, notification);
    }

}
