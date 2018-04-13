package ioedufet.github.shahalihridoy.packageandfnfsuggestiongenerator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ioedufet.github.shahalihridoy.packageandfnfsuggestiongenerator.CallListenerService;

public class AutoStartService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Intent serviceIntent = new Intent(context, MainActivity.class);
            context.startService(serviceIntent);
        }
    }
}
