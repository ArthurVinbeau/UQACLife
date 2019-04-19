package uqac.dim.uqaclife;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import java.util.Calendar;

import static uqac.dim.uqaclife.Appservice.CHANNEL_ID;


public class NotifService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Intent deleteIntent = new Intent(this, MainActivity.class);
        PendingIntent deletePendingIntent = PendingIntent.getService(this,
                4,
                deleteIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        android.app.Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(intent.getStringExtra("nom"))
                .setContentText(intent.getStringExtra("room"))
                .setSmallIcon(R.drawable.mini_ul)
                .setContentIntent(pendingIntent)
                .setDeleteIntent(deletePendingIntent)
                .setOngoing(true)
                .build();
        //NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        //notificationManager.notify(4, notification);

        startForeground(1,notification);
        //do heavy work on a background thread
        //stopSelf();

        return START_NOT_STICKY;
    }

    public String jour(int day)
    {
        switch (day) {
            case (2):
                return "Lundi";
            case (3):
                return "Mardi";
            case (4):
                return "Mercredi";
            case (5):
                return "Jeudi";
            case (6):
                return "Vendredi";
            case (7):
                return "Samedi";
            case (1):
                return "Dimanche";
        }
        return"";

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

