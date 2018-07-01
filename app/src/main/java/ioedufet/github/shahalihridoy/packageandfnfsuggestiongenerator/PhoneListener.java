package ioedufet.github.shahalihridoy.packageandfnfsuggestiongenerator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CallLog;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class PhoneListener extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

//        read message received
        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                    String messageBody = smsMessage.getMessageBody();
                    String number = smsMessage.getOriginatingAddress();
                    Toast.makeText(context,number,Toast.LENGTH_SHORT).show();
                }
            } else {
                Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
                SmsMessage[] msgs = null;
                String msg_from;

                if (bundle != null) {

//                    ---retrieve the SMS message received---
                    try {
                        Object[] pdus = (Object[]) bundle.get("pdus");
                        msgs = new SmsMessage[pdus.length];
                        for (int i = 0; i < msgs.length; i++) {
                            msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                            msg_from = msgs[i].getOriginatingAddress();
                            String msgBody = msgs[i].getMessageBody();
                        }
                    } catch (Exception e) {
//                            Log.d("Exception caught",e.getMessage());
                    }
                }
            }
        }

//        read outgoing call
        else if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(
                TelephonyManager.EXTRA_STATE_IDLE)) {

            @SuppressLint("MissingPermission") Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DATE + " DESC");
            cursor.moveToNext();

            int number = cursor.getColumnIndexOrThrow(CallLog.Calls.NUMBER);
            int duration = cursor.getColumnIndexOrThrow(CallLog.Calls.DURATION);

            String phType = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.TYPE));
            String phNumber = cursor.getString(number);
            String phDuration = cursor.getString(duration);

            String dateStrig = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.DATE));
            Date date = new Date(Long.valueOf(dateStrig));
            String callTime = new SimpleDateFormat("HH:mm").format(date);

            System.out.println(phNumber);
            System.out.println(phDuration);
            System.out.println(callTime);

            if (phDuration.equals("0") || phType.charAt(0) != '2' || phNumber.length() < 11) {
                return;
            } else if (phNumber.charAt(0) == '+')
                phNumber = phNumber.substring(3, phNumber.length());

            Database db = new Database(context, "CallLog", null, 13795);
            db.insertdata(phNumber, phDuration, callTime);
            db.close();

//            History h = new History(new Handler(), context);
//            context.getContentResolver().registerContentObserver(CallLog.Calls.CONTENT_URI, true, h);

        }
    }

}
