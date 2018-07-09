package ioedufet.github.shahalihridoy.packageandfnfsuggestiongenerator;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

public class ServiceWaker extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

//        //<editor-fold desc="Notification">
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
//        builder.setSmallIcon(R.drawable.ic_launcher)
//                .setContentTitle("Package & FnF Suggestion Generator")
//                .setContentText("testing")
//                .setContentIntent(PendingIntent.getActivity(context,
//                        0,
//                        new Intent(context,MainActivity.class)
//                        ,PendingIntent.FLAG_UPDATE_CURRENT))
//                .setAutoCancel(false);
//
//        Notification notification = builder.build();
//        NotificationManagerCompat.from(context).notify(13795,notification);
//        //</editor-fold>

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            new BackgroundServiceMarshmallow(context).startBackgroundService();
        }
        else {
                Intent serviceIntent = new Intent(context, CallListenerService.class);
                context.startService(serviceIntent);
        }
    }
}
