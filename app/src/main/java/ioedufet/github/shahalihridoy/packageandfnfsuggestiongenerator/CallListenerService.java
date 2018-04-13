package ioedufet.github.shahalihridoy.packageandfnfsuggestiongenerator;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

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
                new PhoneListener();
            }
        };
        callTrack.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        callTrack.interrupt();
        super.onDestroy();
    }
}