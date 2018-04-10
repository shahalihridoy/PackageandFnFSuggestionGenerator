package ioedufet.github.shahalihridoy.packageandfnfsuggestiongenerator;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;

import java.text.SimpleDateFormat;
import java.util.Date;

public class History extends ContentObserver {
    Activity activity;
    public History(Handler handler, Activity activity) {
        super(handler);
        this.activity = activity;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);
        System.out.println("content is changed");
        Cursor managedCursor = activity.managedQuery(CallLog.Calls.CONTENT_URI, null, null, null, null);
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
        if (managedCursor.moveToFirst())
            System.out.printf("fdskafag"+managedCursor.getString(number));

    }
    //
//
//    Context c;
//
//    public History(Handler handler, Context cc) {
//        // TODO Auto-generated constructor stub
//        super(handler);
//        c = cc;
//    }
//
//    @Override
//    public boolean deliverSelfNotifications() {
//        return true;
//    }
//
//    @Override
//    public void onChange(boolean selfChange) {
//        // TODO Auto-generated method stub
//        super.onChange(selfChange);
//        SharedPreferences sp = c.getSharedPreferences("CallLog", Activity.MODE_PRIVATE);
//        String number = sp.getString("number", null);
//        if (number != null) {
//            getCalldetailsNow();
//            sp.edit().putString("number", null).commit();
//        }
//    }
//
//    private void getCalldetailsNow() {
//        // TODO Auto-generated method stub
//        @SuppressLint("MissingPermission") Cursor managedCursor = c.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, android.provider.CallLog.Calls.DATE + " DESC");
//        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
//        int duration1 = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
//        int type1 = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
//        int date1 = managedCursor.getColumnIndex(CallLog.Calls.DATE);
//
//        if (managedCursor.moveToFirst()) {
//            String phNumber = managedCursor.getString(number);
//            String callDuration = managedCursor.getString(duration1);
//
//            String type = managedCursor.getString(type1);
//            String date = managedCursor.getString(date1);
//
//            String dir = null;
//            int dircode = Integer.parseInt(type);
//            switch (dircode) {
//                case CallLog.Calls.OUTGOING_TYPE:
//                    dir = "OUTGOING";
//                    break;
//                case CallLog.Calls.INCOMING_TYPE:
//                    dir = "INCOMING";
//                    break;
//                case CallLog.Calls.MISSED_TYPE:
//                    dir = "MISSED";
//                    break;
//                default:
//                    dir = "MISSED";
//                    break;
//            }
//
//            SimpleDateFormat sdf_date = new SimpleDateFormat("dd/MM/yyyy");
//            SimpleDateFormat sdf_time = new SimpleDateFormat("h:mm a");
//            // SimpleDateFormat sdf_dur = new SimpleDateFormat("KK:mm:ss");
//
//            String dateString = sdf_date.format(new Date(Long.parseLong(date)));
//            String timeString = sdf_time.format(new Date(Long.parseLong(date)));
//            //  String duration_new=sdf_dur.format(new Date(Long.parseLong(callDuration)));
//
//            Database db = new Database(c, "CallLog", null, 13795);
//            db.insertdata(phNumber, callDuration, dir);
//
//        }
//
//        managedCursor.close();
//    }
}
