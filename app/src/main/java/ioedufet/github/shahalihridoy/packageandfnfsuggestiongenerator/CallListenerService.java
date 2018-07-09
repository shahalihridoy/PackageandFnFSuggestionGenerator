package ioedufet.github.shahalihridoy.packageandfnfsuggestiongenerator;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

public class CallListenerService extends Service {

    Thread callTrack;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        callTrack = new Thread(){
            @Override
            public void run() {
                //<editor-fold desc="Notification">
                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
                builder.setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("Package & FnF Suggestion Generator")
                        .setContentText("testing")
                        .setContentIntent(PendingIntent.getActivity(getApplicationContext(),
                        0,
                        new Intent(getApplicationContext(),MainActivity.class)
                        ,PendingIntent.FLAG_UPDATE_CURRENT))
                        .setAutoCancel(false);

                Notification notification = builder.build();
                NotificationManagerCompat.from(getApplicationContext()).notify(13795,notification);
                //</editor-fold>
            }
        };
        callTrack.start();
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        callTrack.interrupt();
        super.onDestroy();
    }
}