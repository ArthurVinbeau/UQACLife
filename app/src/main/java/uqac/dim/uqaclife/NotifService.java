package uqac.dim.uqaclife;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Icon;
import android.os.Build;
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
        Notification.Builder builder = new Notification.Builder(getApplicationContext());
        String s = intent.getStringExtra("room");
        String s2 = intent.getStringExtra("start");
        String s3 = intent.getStringExtra("end");
        if(s == null)
            s = "       ";
        if(s2 == null)
            s2 = "";
        if(s3 == null)
            s3 = "";

        Notification notification = builder
                .setContentTitle(intent.getStringExtra("nom"))
                .setContentText(intent.getStringExtra("room"))
//                    .setSmallIcon(R.drawable.mini_ul)
                .setSmallIcon(Icon.createWithBitmap(createBitmap(s.substring(3, 5), s.substring(5, 7), false)))
//                    .setLargeIcon(Bitmap.createBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_ulrond2)))
                .setLargeIcon(Icon.createWithBitmap(createBitmap(s2, s3, true)))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();
        //NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        //notificationManager.notify(4, notification);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID);
        }

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationManager.createNotificationChannel(channel);
        }

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
    private Bitmap createBitmap(String text1, String text2 , Boolean square){
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(50);
        paint.setColor(Color.parseColor("#000000"));
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text1)); // round
        int height = (int) (baseline + paint.descent() );
        Bitmap image ;
        if(square)
            image = Bitmap.createBitmap(Math.max(width,2*height-20), Math.max(width,2*height), Bitmap.Config.ARGB_8888);
        else
            image = Bitmap.createBitmap(width, 2*height - 20, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawText(text1, 0, baseline -10, paint);
        canvas.drawText(text2, 0,height + baseline -10, paint);
        return image;
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

