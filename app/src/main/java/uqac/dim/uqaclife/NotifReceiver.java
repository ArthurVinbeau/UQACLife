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
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.Random;

public class NotifReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "1234";

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getBooleanExtra("service",false))
        {
            Intent serviceintent = new Intent(context, NotifService.class);
            serviceintent.putExtra("nom", intent.getStringExtra("nom"));
            serviceintent.putExtra("room", intent.getStringExtra("room"));
            ContextCompat.startForegroundService(context, serviceintent);
        }
        else
        {

            Intent notificationIntent = new Intent(context, NotificationActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(MainActivity.class);
            stackBuilder.addNextIntent(notificationIntent);
            PendingIntent pendingIntent = stackBuilder.getPendingIntent(100, PendingIntent.FLAG_UPDATE_CURRENT);
            Notification.Builder builder = new Notification.Builder(context);
            Notification notification = builder
                    .setContentTitle(intent.getStringExtra("nom"))
                    .setContentText(intent.getStringExtra("room"))
//                    .setSmallIcon(R.drawable.mini_ul)
                    .setSmallIcon(Icon.createWithBitmap(createBitmap(intent.getStringExtra("room").substring(3, 5), intent.getStringExtra("room").substring(5, 7), false)))
//                    .setLargeIcon(Bitmap.createBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_ulrond2)))
                    .setLargeIcon(Icon.createWithBitmap(createBitmap(intent.getStringExtra("start"), intent.getStringExtra("end"), true)))
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
                        NotificationManager.IMPORTANCE_HIGH
                );
                notificationManager.createNotificationChannel(channel);
            }
            notificationManager.notify(new Random().nextInt(10), notification);
        }

    }



    private Bitmap createBitmap(String text, Boolean square){
        //text = "a\na";
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(50);
        paint.setColor(Color.parseColor("#000000"));
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text)); // round
        int height = (int) (baseline + paint.descent() );
        Bitmap image ;
        if(square)
            image = Bitmap.createBitmap(Math.max(width,height), Math.max(width,height), Bitmap.Config.ARGB_8888);
        else
            image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
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

}
