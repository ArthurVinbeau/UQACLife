package uqac.dim.uqaclife;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

public class Notification extends ContextWrapper {

    private static final String Channel_id = "1234";
    private static final String Channel_name = "Channel";
    private NotificationManager nm;
    public Notification(Context b)
    {
        super(b);
        createNotificationChannel();
    }
    private void createNotificationChannel()  {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String description ="test";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(Channel_id, Channel_name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public NotificationManager getManager()
    {
        if(nm == null)
            nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        return nm;
    }

    public NotificationCompat.Builder getnotif(String title, String body)
    {
        return new NotificationCompat.Builder(getApplicationContext(),Channel_id)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.mipmap.application_image)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            // Set the intent that will fire when the user taps the notification
            .setAutoCancel(true);
    }
}
