package ioedufet.github.shahalihridoy.packageandfnfsuggestiongenerator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class MessageListener extends BroadcastReceiver {

    String messageBody;
    String from;

    @Override
    public void onReceive(Context context, Intent intent) {

        messageBody = "";


        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

                for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
//                    getting the full message by appending pars
                    messageBody += smsMessage.getMessageBody();
                    from = smsMessage.getOriginatingAddress();
                }

                MainActivity.msgbody = messageBody;
                MainActivity.from = from;
                System.out.println(messageBody);

//            when fnf list is received, analyse it
                for (String number : messageBody.split("\\r?\\n"))
                    if (!number.equals("")) {
                        number = number.trim();
                        if (number.charAt(0) == '0')
                            System.out.println(":" + number + ":");
                    }

            } else {
                Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
                SmsMessage[] msgs = null;
                String msg_from = "";

                if (bundle != null) {

//                    ---retrieve the SMS message received---
                    try {
                        Object[] pdus = (Object[]) bundle.get("pdus");
                        msgs = new SmsMessage[pdus.length];
                        for (int i = 0; i < msgs.length; i++) {
                            msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                            MainActivity.from = msgs[i].getOriginatingAddress();
//                            getting full message by appending parts
                            messageBody += msgs[i].getMessageBody();
                        }
                        MainActivity.msgbody = messageBody;
                    } catch (Exception e) {
                        Toast.makeText(context, "Couldn't read message", Toast.LENGTH_LONG).show();
                    }
                }

                from = msg_from;
            }

            messageBody = messageBody.toLowerCase();

            if (from.equals("GP") && (messageBody.contains("ondhu") || messageBody.contains("chinto") || messageBody.contains("smile") || messageBody.contains("juice"))) {

                final String MY_PREFS_NAME = "Package & FnF Suggestion Generator";
                SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();

//            save to preference
                editor.putString("currentPackage", messageBody);
                editor.apply();
                editor.commit();

            }
        }
    }
}
