package ioedufet.github.shahalihridoy.packageandfnfsuggestiongenerator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class ServiceWaker extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            new BackgroundServiceMarshmallow(context).startBackgroundService();
        }
        else {
                Intent serviceIntent = new Intent(context, CallListenerService.class);
                context.startService(serviceIntent);
        }
    }
}
