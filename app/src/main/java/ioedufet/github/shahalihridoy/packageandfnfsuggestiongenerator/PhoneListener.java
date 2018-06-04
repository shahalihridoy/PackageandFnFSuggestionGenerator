package ioedufet.github.shahalihridoy.packageandfnfsuggestiongenerator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CallLog;
import android.provider.Telephony;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PhoneListener extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(
                TelephonyManager.EXTRA_STATE_IDLE)) {

            @SuppressLint("MissingPermission") Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DATE+" DESC");
            cursor.moveToNext();

            int number = cursor.getColumnIndexOrThrow(CallLog.Calls.NUMBER);
            int duration = cursor.getColumnIndexOrThrow(CallLog.Calls.DURATION);

            String phType = cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE));
            String phNumber = cursor.getString(number);
            String phDuration = cursor.getString(duration);

            String dateStrig = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.DATE));
            Date date = new Date(Long.valueOf(dateStrig));
            String callTime = new SimpleDateFormat("HH:mm").format(date);

            System.out.println(phNumber);
            System.out.println(phDuration);
            System.out.println(callTime);

            if(phDuration.equals("0") || phType.charAt(0)== '2' || phNumber.length()<11){
                return;
            }
            else if(phNumber.charAt(0) == '+')
                phNumber = phNumber.substring(3, phNumber.length());

            Database db = new Database(context, "CallLog", null, 13795);
            db.insertdata(phNumber,phDuration,callTime);
            db.close();

//            History h = new History(new Handler(), context);
//            context.getContentResolver().registerContentObserver(CallLog.Calls.CONTENT_URI, true, h);

        }
    }
}
