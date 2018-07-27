package ioedufet.github.shahalihridoy.packageandfnfsuggestiongenerator;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class DataLoader extends Activity {
    Context context;
    Activity activity;
    Cursor cursor;
    Database db;
    Boolean isGranted = false;

    static final String MY_PREFS_NAME = "Package & FnF Suggestion Generator";

    public DataLoader(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    @SuppressLint("MissingPermission")
    public void insertCallListToDatabase() {

        db = new Database(context, "CallLog", null, 13795);
        cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, null);

//        check if database already exists
        if (db.tenSecondPulse().getCount() != 0)
            return;

        int number = cursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = cursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = cursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = cursor.getColumnIndex(CallLog.Calls.DURATION);

//        save the first call date into preference
        cursor.moveToFirst();
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("startDate",cursor.getString(date));
        editor.apply();
        editor.commit();

        cursor.moveToFirst();
        do {
            String phNumber = cursor.getString(number);
            String callType = cursor.getString(type);
            String callDate = cursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callTime = new SimpleDateFormat("HH:mm").format(callDayTime);
            String callDuration = cursor.getString(duration);

            int dircode = Integer.parseInt(callType);

            if (dircode != CallLog.Calls.OUTGOING_TYPE || callDuration.equals("0") || phNumber.length() < 11)
                continue;

            try {
                if (phNumber.charAt(0) == '+')
                    phNumber = phNumber.substring(3, phNumber.length());
            } catch (Exception e) {
                continue;
            }

//            inserting in database
            db.insertdata(phNumber, callDuration, callTime);
            db.close();

//            System.out.println(phNumber);
//            System.out.println(callDuration);
//            System.out.println(new SimpleDateFormat("HH:mm").format(callDayTime));
        } while (cursor.moveToNext());
        cursor.close();
    }

//    public void getCallDetailsFromDatabase() {
//
//        db = new Database(context, "CallLog", null, 13795);
//        StringBuffer sb = new StringBuffer();
//        cursor = db.tenSecondPulse();
//
//        if (cursor.moveToFirst()) {
//            do {
//                sb.append("\nPhone Number:--- " + cursor.getString(0) + " \nCall duration in sec :--- " + cursor.getString(1));
//                sb.append("\n----------------------------------");
//            } while (cursor.moveToNext());
//        }
//
//        db.close();
//        cursor.close();
//    }

    Random random = new Random();
    String[] operator = {"017", "018", "016", "019", "015"};
    StringBuilder fakeDate = new StringBuilder(); // 6 digit should be added to this
    int opNo; //operator number
    String fakeNumber = "";
    String fakeDuration = "";

    public void fakeCallLog() {

//        insertPlaceholderCall(context,context.getContentResolver(),"01763413041","3000",1545765415102L,2);

        for (int i = 0; i < 50; i++) {
            fakeDate.delete(0, fakeDate.length());
            fakeDate.append("1445" + Integer.toString(random.nextInt(899999999) + 100000000));
            opNo = random.nextInt(4);
            fakeNumber = operator[opNo] + Integer.toString(random.nextInt(899999) + 100000) + Integer.toString(random.nextInt(90) + 10);
            fakeDuration = Integer.toString(random.nextInt(600) + 60);

            insertPlaceholderCall(context, context.getContentResolver(), fakeNumber, fakeDuration, Long.parseLong(fakeDate.toString()), 2);
            System.out.println("Fake Call Log: " + fakeNumber + " , " + fakeDuration + " , " + new SimpleDateFormat("HH:mm").format(Long.parseLong(fakeDate.toString())));
        }
    }


    @SuppressLint("MissingPermission")
    public void insertPlaceholderCall(Context context, ContentResolver contentResolver, String number, String duration, long date, int type) {
        ContentValues values = new ContentValues();
        values.put(CallLog.Calls.NUMBER, number);
        values.put(CallLog.Calls.DATE, date);
        values.put(CallLog.Calls.DURATION, duration);
        values.put(CallLog.Calls.TYPE, type);
        values.put(CallLog.Calls.NEW, 1);
        values.put(CallLog.Calls.CACHED_NAME, "");
        values.put(CallLog.Calls.CACHED_NUMBER_TYPE, 0);
        values.put(CallLog.Calls.CACHED_NUMBER_LABEL, "");
//        Log.d("TAG", "Number :" + number + "Duration :" + duration + "Date :" + date + "Type :" + String.valueOf(type));
        try {
            contentResolver.insert(CallLog.Calls.CONTENT_URI, values);
//            System.out.println(number);
        } catch (Exception e) {
            System.out.println(e);
        }

    }

}
