package ioedufet.github.shahalihridoy.packageandfnfsuggestiongenerator;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CallLog;
import android.provider.Telephony;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class PhoneListener extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(
                TelephonyManager.EXTRA_STATE_IDLE)) {

            History h = new History(new Handler(), context);
            context.getContentResolver().registerContentObserver(CallLog.Calls.CONTENT_URI, true, h);

        }
    }
}
