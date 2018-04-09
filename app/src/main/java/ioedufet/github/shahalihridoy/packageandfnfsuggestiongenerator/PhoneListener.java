package ioedufet.github.shahalihridoy.packageandfnfsuggestiongenerator;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class PhoneListener extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(
                TelephonyManager.EXTRA_STATE_RINGING)) {

            // Phone number
            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            System.out.println("call accepted "+incomingNumber);
            // Ringing state
            // This code will execute when the phone has an incoming call
        } else if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(
                TelephonyManager.EXTRA_STATE_IDLE)
                || intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(
                TelephonyManager.EXTRA_STATE_OFFHOOK)) {
            System.out.println(intent.getAction());
            System.out.println("call rejected "+intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER));
            // This code will execute when the call is answered or disconnected
        }
    }
}
